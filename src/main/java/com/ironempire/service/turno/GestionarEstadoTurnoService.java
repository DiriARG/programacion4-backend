package com.ironempire.service.turno;

import com.ironempire.dto.response.turno.AlumnoInscriptoResponse;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.TurnoMapper;
import com.ironempire.model.AlumnoTurno;
import com.ironempire.model.Turno;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaAlumnoTurnoRepository;
import com.ironempire.repository.JpaTurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GestionarEstadoTurnoService {

    private final JpaTurnoRepository turnoRepository;
    private final JpaAlumnoTurnoRepository alumnoTurnoRepository;
    private final TurnoMapper turnoMapper;

    @Transactional
    public TurnoResponse desactivarTurno(Long id) {

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un turno con el ID: " + id));

        if (!turno.getActivo()) {
            throw new RecursoInvalidoException("El turno ya se encuentra inactivo.");
        }

        turno.setActivo(false);

        Turno turnoDesactivado = turnoRepository.save(turno);

        List<AlumnoInscriptoResponse> alumnos = alumnoTurnoRepository.findByTurnoId(id)
                .stream()
                .map(AlumnoTurno::getAlumno)
                .map(alumno -> new AlumnoInscriptoResponse(
                        alumno.getId(),
                        alumno.getNombre(),
                        alumno.getApellido(),
                        alumno.getDni()))
                .toList();

        return turnoMapper.convertirAResponse(turnoDesactivado, alumnos);
    }

    @Transactional
    public TurnoResponse reactivarTurno(Long id) {

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un turno con el ID: " + id));

        if (turno.getActivo()) {
            throw new RecursoInvalidoException("El turno ya se encuentra activo.");
        }

        if (!turno.getProfesor().getActivo()) {
            throw new RecursoInvalidoException("El profesor asociado al turno se encuentra inactivo.");
        }

        boolean existeSuperposicionProfesor = turnoRepository.existeSuperposicionDeProfesor(
                turno.getProfesor().getId(),
                turno.getDiaSemana(),
                turno.getHoraFin(),
                turno.getHoraInicio(),
                id);

        if (existeSuperposicionProfesor) {
            throw new RecursoInvalidoException(
                    "El turno no puede reactivarse porque se superpone con otro turno activo del profesor.");
        }

        // Obtiene los alumnos que ya estaban inscriptos en el turno.
        List<AlumnoTurno> inscripciones = alumnoTurnoRepository.findByTurnoId(id);

        List<Long> alumnoIds = inscripciones.stream()
                .map(AlumnoTurno::getAlumno)
                .map(Usuario::getId)
                .toList();

        if (!alumnoIds.isEmpty()) {

            long cantidadSuperposiciones = alumnoTurnoRepository.contarSuperposicionesDeAlumnos(
                    alumnoIds,
                    id,
                    turno.getDiaSemana(),
                    turno.getHoraFin(),
                    turno.getHoraInicio());

            if (cantidadSuperposiciones > 0) {
                throw new RecursoInvalidoException(
                        "El turno no puede reactivarse porque se superpone con otro turno activo de al menos uno de los alumnos inscriptos.");
            }
        }

        turno.setActivo(true);

        Turno turnoReactivado = turnoRepository.save(turno);

        List<AlumnoInscriptoResponse> alumnos = inscripciones.stream()
                .map(AlumnoTurno::getAlumno)
                .map(alumno -> new AlumnoInscriptoResponse(
                        alumno.getId(),
                        alumno.getNombre(),
                        alumno.getApellido(),
                        alumno.getDni()))
                .toList();

        return turnoMapper.convertirAResponse(turnoReactivado, alumnos);
    }
}