package com.ironempire.service.turno;

import com.ironempire.dto.request.turno.ModificarTurnoRequest;
import com.ironempire.dto.response.turno.AlumnoInscriptoResponse;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.TurnoMapper;
import com.ironempire.model.AlumnoTurno;
import com.ironempire.model.Turno;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaAlumnoTurnoRepository;
import com.ironempire.repository.JpaTurnoRepository;
import com.ironempire.service.usuario.ValidarUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModificarTurnoService {

    private final JpaTurnoRepository turnoRepository;
    private final JpaAlumnoTurnoRepository alumnoTurnoRepository;
    private final ValidarUsuarioService validarUsuarioService;
    private final TurnoMapper turnoMapper;

    @Transactional
    public TurnoResponse modificarTurno(Long id, ModificarTurnoRequest request) {

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un turno con el ID: " + id));

        if (!turno.getActivo()) {
            throw new RecursoInvalidoException("El turno se encuentra inactivo.");
        }

        // Valida que el usuario seleccionado sea un profesor.
        Usuario profesor = validarUsuarioService.validarUsuario(request.getProfesorId(), Rol.PROFESOR, "profesor");

        if (!profesor.getActivo()) {
            throw new RecursoInvalidoException("El profesor se encuentra inactivo.");
        }

        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new RecursoInvalidoException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        boolean existeSuperposicionProfesor = turnoRepository.existeSuperposicionDeProfesor(
                profesor.getId(),
                request.getDiaSemana(),
                request.getHoraFin(),
                request.getHoraInicio(),
                id);

        if (existeSuperposicionProfesor) {
            throw new RecursoInvalidoException( "El profesor ya posee otro turno activo superpuesto en ese día y horario.");
        }

        // Obtiene los alumnos que ya están inscriptos en el turno.
        List<AlumnoTurno> inscripciones = alumnoTurnoRepository.findByTurnoId(id);

        List<Long> alumnoIds = inscripciones.stream()
                .map(AlumnoTurno::getAlumno)
                .map(Usuario::getId)
                .toList();

        // Solo se verifica la superposición si existen alumnos inscriptos en el turno.
        if (!alumnoIds.isEmpty()) {

            long cantidadSuperposiciones = alumnoTurnoRepository.contarSuperposicionesDeAlumnos(
                    alumnoIds,
                    id,
                    request.getDiaSemana(),
                    request.getHoraFin(),
                    request.getHoraInicio());

            if (cantidadSuperposiciones > 0) {
                throw new RecursoInvalidoException(
                        "El nuevo horario se superpone con otro turno activo de al menos uno de los alumnos inscriptos.");
            }
        }

        turno.setProfesor(profesor);
        turno.setNombreClase(request.getNombreClase());
        turno.setDiaSemana(request.getDiaSemana());
        turno.setHoraInicio(request.getHoraInicio());
        turno.setHoraFin(request.getHoraFin());

        Turno turnoModificado = turnoRepository.save(turno);

        // Mantiene en la respuesta los alumnos que ya estaban inscriptos.
        List<AlumnoInscriptoResponse> alumnos = inscripciones.stream()
                .map(AlumnoTurno::getAlumno)
                .map(alumno -> new AlumnoInscriptoResponse(
                        alumno.getId(),
                        alumno.getNombre(),
                        alumno.getApellido(),
                        alumno.getDni()))
                .toList();

        return turnoMapper.convertirAResponse(turnoModificado, alumnos);
    }
}