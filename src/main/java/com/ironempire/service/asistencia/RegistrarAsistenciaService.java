package com.ironempire.service.asistencia;

import com.ironempire.dto.request.asistencia.RegistrarAsistenciaRequest;
import com.ironempire.dto.response.asistencia.AsistenciaResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.AsistenciaMapper;
import com.ironempire.model.Asistencia;
import com.ironempire.model.Turno;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaAlumnoTurnoRepository;
import com.ironempire.repository.JpaAsistenciaRepository;
import com.ironempire.repository.JpaTurnoRepository;
import com.ironempire.repository.JpaUsuarioRepository;
import com.ironempire.service.usuario.ValidarUsuarioService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrarAsistenciaService {

    private final JpaAsistenciaRepository asistenciaRepository;
    private final JpaUsuarioRepository usuarioRepository;
    private final JpaTurnoRepository turnoRepository;
    private final JpaAlumnoTurnoRepository alumnoTurnoRepository;
    private final ValidarUsuarioService validarUsuarioService;
    private final AsistenciaMapper asistenciaMapper;

    @Transactional
    public AsistenciaResponse registrarAsistencia(RegistrarAsistenciaRequest request, String emailAutenticado) {

        Usuario usuarioAutenticado = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario autenticado."));

        Usuario alumno = validarUsuarioService.validarUsuario(
                request.getAlumnoId(),
                Rol.ALUMNO,
                "alumno");

        // Solo se pueden registrar nuevas asistencias para alumnos activos.
        if (!alumno.getActivo()) {
            throw new RecursoInvalidoException("El alumno se encuentra inactivo.");
        }

        // El turno es opcional: null significa entrenamiento libre.
        Turno turno = null;

        // Validaciones de una asistencia cuando el alumno asiste a un turno.
        if (request.getTurnoId() != null) {

            turno = turnoRepository.findById(request.getTurnoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontró un turno con el ID: " + request.getTurnoId()));

            if (!turno.getActivo()) {
                throw new RecursoInvalidoException("El turno se encuentra inactivo.");
            }

            // El alumno debe estar inscripto en el turno seleccionado.
            if (!alumnoTurnoRepository.existsByAlumnoIdAndTurnoId(alumno.getId(), turno.getId())) {

                throw new RecursoInvalidoException("El alumno no se encuentra inscripto en el turno indicado.");
            }
        }

        // Se construye la asistencia con los datos validados.
        Asistencia asistencia = new Asistencia();
        asistencia.setAlumno(alumno);
        asistencia.setTurno(turno);
        asistencia.setRegistradoPor(usuarioAutenticado);
        asistencia.setFecha(request.getFecha());
        asistencia.setHora(request.getHora());

        Asistencia asistenciaGuardada = asistenciaRepository.save(asistencia);

        return asistenciaMapper.convertirAResponse(asistenciaGuardada);
    }
}