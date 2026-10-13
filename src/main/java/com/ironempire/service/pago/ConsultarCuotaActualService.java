package com.ironempire.service.pago;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ironempire.dto.response.pago.CuotaActualResponse;
import com.ironempire.enums.EstadoCuota;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.model.Pago;
import com.ironempire.model.Plan;
import com.ironempire.repository.JpaPagoRepository;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultarCuotaActualService {

        private final JpaPagoRepository jpaPagoRepository;
        private final JpaUsuarioRepository jpaUsuarioRepository;
        private final CalendarioCuotas calendario;

        @Transactional(readOnly = true)
        public CuotaActualResponse consultarCuotaActual(String email) {

                Long alumnoId = jpaUsuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "No se encontró el perfil del usuario autenticado."))
                                .getId();

                // RN-79: el plan vigente sale del último pago no anulado.
                Optional<Pago> ultimoPagoOptional = jpaPagoRepository
                                .findTopByAlumnoIdAndAnuladoFalseOrderByFechaVencimientoDesc(alumnoId);

                if (ultimoPagoOptional.isEmpty()) {
                        return CuotaActualResponse.sinPlanVigente();
                }

                Plan planVigente = ultimoPagoOptional.get().getPlan();

                YearMonth periodoActual = calendario.periodoActual();
                LocalDate fechaVencimiento = calendario.vencimientoDe(periodoActual); // fin del período
                LocalDate fechaLimitePago = calendario.limitePagoDe(periodoActual); // día 10

                Optional<Pago> pagoPeriodoActual = jpaPagoRepository
                                .findByAlumnoIdAndFechaVencimientoAndAnuladoFalse(alumnoId, fechaVencimiento);

                if (pagoPeriodoActual.isPresent()) {
                        Pago pago = pagoPeriodoActual.get();
                        return new CuotaActualResponse(
                                        true,
                                        planVigente.getNombre(),
                                        planVigente.getPrecioMensual(),
                                        pago.getMontoAbonado(),
                                        pago.getFechaPago(),
                                        fechaVencimiento,
                                        fechaLimitePago,
                                        EstadoCuota.PAGADO);
                }

                EstadoCuota estado = !calendario.hoy().isAfter(fechaLimitePago)
                                ? EstadoCuota.PENDIENTE
                                : EstadoCuota.VENCIDO;

                return new CuotaActualResponse(
                                true,
                                planVigente.getNombre(),
                                planVigente.getPrecioMensual(),
                                null,
                                null,
                                fechaVencimiento,
                                fechaLimitePago,
                                estado);
        }
}
