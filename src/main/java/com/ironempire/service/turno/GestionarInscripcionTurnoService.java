package com.ironempire.service.turno;

import com.ironempire.dto.response.turno.AlumnoInscriptoResponse;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoExistenteException;
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
public class GestionarInscripcionTurnoService {

    private final ValidarUsuarioService validarUsuarioService;
    private final JpaTurnoRepository turnoRepository;
    private final JpaAlumnoTurnoRepository alumnoTurnoRepository;
    private final TurnoMapper turnoMapper;

    @Transactional
    public TurnoResponse inscribirAlumno(Long turnoId, Long alumnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un turno con el ID: " + turnoId));

        if (!turno.getActivo()) {
            throw new RecursoInvalidoException("El turno se encuentra inactivo.");
        }

        Usuario alumno = validarUsuarioService.validarUsuario(alumnoId, Rol.ALUMNO, "alumno");

        if (!alumno.getActivo()) {
            throw new RecursoInvalidoException("El alumno se encuentra inactivo.");
        }

        if (alumnoTurnoRepository.existsByAlumnoIdAndTurnoId(alumnoId, turnoId)) {
            throw new RecursoExistenteException("El alumno ya se encuentra inscripto en el turno.");
        }

        boolean existeSuperposicion = alumnoTurnoRepository.existeSuperposicionDeAlumno(
                alumnoId,
                turnoId,
                turno.getDiaSemana(),
                turno.getHoraFin(),
                turno.getHoraInicio());

        if (existeSuperposicion) {
            throw new RecursoInvalidoException(
                    "El alumno ya posee otro turno activo superpuesto en ese día y horario.");
        }

        // Crea la relación entre el alumno y el turno.
        AlumnoTurno alumnoTurno = new AlumnoTurno();
        alumnoTurno.setAlumno(alumno);
        alumnoTurno.setTurno(turno);

        alumnoTurnoRepository.save(alumnoTurno);

        List<AlumnoInscriptoResponse> alumnos = alumnoTurnoRepository.findByTurnoId(turnoId)
                .stream()
                .map(AlumnoTurno::getAlumno)
                .map(usuario -> new AlumnoInscriptoResponse(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getDni()))
                .toList();

        return turnoMapper.convertirAResponse(turno, alumnos);
    }

    @Transactional
    public TurnoResponse quitarAlumno(Long turnoId, Long alumnoId) {

        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un turno con el ID: " + turnoId));

        // La baja solamente puede realizarse sobre un turno activo.
        if (!turno.getActivo()) {
            throw new RecursoInvalidoException("El turno se encuentra inactivo.");
        }

        validarUsuarioService.validarUsuario(alumnoId, Rol.ALUMNO, "alumno");

        // Busca la inscripción existente.
        AlumnoTurno alumnoTurno = alumnoTurnoRepository.findByAlumnoIdAndTurnoId(
                alumnoId,
                turnoId)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("El alumno no se encuentra inscripto en el turno."));

        alumnoTurnoRepository.delete(alumnoTurno);

        List<AlumnoInscriptoResponse> alumnos = alumnoTurnoRepository.findByTurnoId(turnoId)
                .stream()
                .map(AlumnoTurno::getAlumno)
                .map(usuario -> new AlumnoInscriptoResponse(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getDni()))
                .toList();

        return turnoMapper.convertirAResponse(turno, alumnos);
    }
}