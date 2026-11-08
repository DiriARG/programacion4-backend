package com.ironempire.service.notificacion;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ironempire.enums.Rol;
import com.ironempire.model.AvisoCuotaPendiente;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaAsistenciaRepository;
import com.ironempire.repository.JpaAvisoCuotaPendienteRepository;
import com.ironempire.repository.JpaPagoRepository;
import com.ironempire.repository.JpaUsuarioRepository;
import com.ironempire.service.pago.CalendarioCuotas;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Aviso de cuota pendiente.
 *
 * Todos los días, a partir del día 11 del mes, avisa por mail a los alumnos activos que:
 *  - tuvieron al menos una asistencia en el mes en curso, y
 *  - no tienen un pago vigente (no anulado) del período actual, y
 *  - no fueron avisados antes por este período.
 *
 * Corre todos los días (y no solo el 11) para alcanzar también a quien asiste por primera vez
 * después del 11, y para reintentar si un envío falló. El registro en aviso_cuota_pendiente
 * garantiza un único aviso por alumno y período.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AvisoCuotaPendienteService {

    private final CalendarioCuotas calendario;
    private final JpaAsistenciaRepository jpaAsistenciaRepository;
    private final JpaPagoRepository jpaPagoRepository;
    private final JpaAvisoCuotaPendienteRepository jpaAvisoCuotaPendienteRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final MailService mailService;
    private final PlantillasMail plantillasMail;

    // Por defecto, todos los días a las 9:00 (hora de Argentina). Se puede cambiar con gym.avisos.cron.
    @Scheduled(cron = "${gym.avisos.cron:0 0 9 * * *}", zone = "America/Argentina/Buenos_Aires")
    public void ejecutarDiariamente() {
        enviarAvisos(calendario.hoy());
    }

    // Separado del @Scheduled para poder probarlo con cualquier fecha.
    public int enviarAvisos(LocalDate hoy) {

        if (hoy.getDayOfMonth() <= CalendarioCuotas.DIA_LIMITE_PAGO) {
            return 0;
        }

        YearMonth periodo = YearMonth.from(hoy);
        LocalDate vencimiento = calendario.vencimientoDe(periodo);
        LocalDate limitePago = calendario.limitePagoDe(periodo);

        List<Long> alumnoIds = jpaAsistenciaRepository
                .findAlumnoIdsConAsistenciaEntre(periodo.atDay(1), hoy);

        int enviados = 0;

        for (Usuario alumno : jpaUsuarioRepository.findAllById(alumnoIds)) {

            if (alumno.getRol() != Rol.ALUMNO || !Boolean.TRUE.equals(alumno.getActivo())) {
                continue;
            }
            if (jpaPagoRepository.existsByAlumnoIdAndFechaVencimientoAndAnuladoFalse(alumno.getId(), vencimiento)) {
                continue;
            }
            if (jpaAvisoCuotaPendienteRepository.existsByAlumnoIdAndFechaVencimiento(alumno.getId(), vencimiento)) {
                continue;
            }

            try {
                PlantillasMail.Contenido contenido = plantillasMail.avisoCuotaPendiente(alumno, periodo, limitePago);
                mailService.enviarHtml(alumno.getEmail(), contenido.asunto(), contenido.html());

                // Se registra recién después de enviar: si el envío falla, mañana se reintenta.
                jpaAvisoCuotaPendienteRepository.save(
                        new AvisoCuotaPendiente(null, alumno, vencimiento, calendario.ahora()));
                enviados++;

            } catch (RuntimeException e) {
                // Un alumno con problemas no debe frenar el aviso a los demás.
                log.warn("No se pudo avisar la cuota pendiente al alumno {}: {}", alumno.getId(), e.getMessage());
            }
        }

        log.info("Avisos de cuota pendiente del período {}: {} enviados", periodo, enviados);
        return enviados;
    }
}
