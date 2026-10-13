
package com.ironempire.service.asistencia;

import com.ironempire.dto.request.asistencia.RegistrarAsistenciaRequest;
import com.ironempire.dto.response.asistencia.AsistenciaResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoExistenteException;
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

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

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
    public AsistenciaResponse registrarAsistencia(RegistrarAsistenciaRequest request, String email) {

        Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario autenticado."));

        Usuario alumno = validarUsuarioService.validarUsuario(
                request.getAlumnoId(),
                Rol.ALUMNO,
                "alumno");

        // Solo se pueden registrar nuevas asistencias para alumnos activos.
        if (!alumno.getActivo()) {
            throw new RecursoInvalidoException("El alumno se encuentra inactivo.");
        }

        // El turno es opcional: null representa un entrenamiento libre.
        Turno turno = null;

        // Validaciones de una asistencia cuando el alumno asiste a un turno.
        if (request.getTurnoId() != null) {

            turno = turnoRepository.findById(request.getTurnoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontró un turno con el ID: " + request.getTurnoId()));

            if (!turno.getActivo()) {
                throw new RecursoInvalidoException("El turno se encuentra inactivo.");
            }

            // Si registra un profesor, el turno debe pertenecerle.
            // Los administradores pueden registrar asistencia en cualquier turno.
            if (usuarioAutenticado.getRol() == Rol.PROFESOR
                    && !Objects.equals(
                            turno.getProfesor().getId(),
                            usuarioAutenticado.getId())) {

                throw new RecursoInvalidoException(
                        "El turno no pertenece al profesor autenticado.");
            }

            // El alumno debe estar inscripto en el turno seleccionado.
            if (!alumnoTurnoRepository.existsByAlumnoIdAndTurnoId(alumno.getId(), turno.getId())) {

                throw new RecursoInvalidoException("El alumno no se encuentra inscripto en el turno indicado.");
            }

            // La fecha debe corresponder al día de la semana del turno.
            if (!correspondeDiaDelTurno(request.getFecha(), turno)) {
                throw new RecursoInvalidoException(
                        "La fecha de asistencia no corresponde al día de la semana del turno.");
            }

            /*
             * La asistencia se permite registrarse desde 30 minutos antes del inicio de la
             * clase hasta la finalización del turno, ambos límites inclusives.
             */
            LocalTime horaMinima = turno.getHoraInicio().minusMinutes(30);

            if (request.getHora().isBefore(horaMinima) || request.getHora().isAfter(turno.getHoraFin())) {

                throw new RecursoInvalidoException(
                        "La hora de asistencia debe encontrarse entre 30 minutos antes del inicio y el fin del turno.");
            }
        }

        // Regla para evitar duplicados en la misma fecha y hora con precisión de
        // minutos.
        LocalTime horaInicioMinuto = request.getHora().withSecond(0).withNano(0);

        LocalTime horaFinMinuto = horaInicioMinuto.withSecond(59).withNano(999_999_999);

        if (asistenciaRepository.existeAsistenciaEnMismoMinuto(
                alumno.getId(),
                request.getFecha(),
                horaInicioMinuto,
                horaFinMinuto)) {

            throw new RecursoExistenteException(
                    "El alumno ya tiene una asistencia registrada para esa fecha y hora.");
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

    /*
     * Valida si el día de la semana de la fecha ingresada coincide con el día
     * programado para el turno.
     */
    private boolean correspondeDiaDelTurno(LocalDate fecha, Turno turno) {

        // Obtiene el nombre completo del día de la semana en español Argentina.
        String diaFecha = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-AR"));

        String diaFechaNormalizado = normalizarDia(diaFecha);
        String diaTurnoNormalizado = normalizarDia(turno.getDiaSemana().name());

        return diaFechaNormalizado.equals(diaTurnoNormalizado);
    }

    /*
     * Normaliza el nombre del día, eliminando las marcas diacríticas (como las
     * tildes) y convirtiéndolo a mayúsculas.
     */
    private String normalizarDia(String dia) {
        return Normalizer
                .normalize(dia, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT);
    }
}
