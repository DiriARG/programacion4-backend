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

        private static final int DIA_VENCIMIENTO = 10;
        private final JpaPagoRepository jpaPagoRepository;
        private final JpaUsuarioRepository jpaUsuarioRepository;

        @Transactional(readOnly = true)
        public CuotaActualResponse consultarCuotaActual(String email) {

                Long alumnoId = jpaUsuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "No se encontró el perfil del usuario autenticado."))
                                .getId();

                Optional<Pago> ultimoPagoOptional = jpaPagoRepository
                                .findTopByAlumnoIdOrderByFechaVencimientoDesc(alumnoId);

                if (ultimoPagoOptional.isEmpty()) {
                        return CuotaActualResponse.sinPlanVigente();
                }

                Plan planVigente = ultimoPagoOptional.get().getPlan();

                YearMonth periodoActual = YearMonth.now();
                LocalDate vencimientoPeriodoActual = periodoActual.atDay(DIA_VENCIMIENTO);

                Optional<Pago> pagoPeriodoActual = jpaPagoRepository.findByAlumnoIdAndFechaVencimiento(
                                alumnoId, vencimientoPeriodoActual);

                if (pagoPeriodoActual.isPresent()) {
                        Pago pago = pagoPeriodoActual.get();
                        return new CuotaActualResponse(
                                        true,
                                        planVigente.getNombre(),
                                        planVigente.getPrecioMensual(),
                                        pago.getMontoAbonado(),
                                        pago.getFechaPago(),
                                        pago.getFechaVencimiento(),
                                        EstadoCuota.PAGADO);
                }
                EstadoCuota estado = !LocalDate.now().isAfter(vencimientoPeriodoActual)
                                ? EstadoCuota.PENDIENTE
                                : EstadoCuota.VENCIDO;

                return new CuotaActualResponse(
                                true,
                                planVigente.getNombre(),
                                planVigente.getPrecioMensual(),
                                null,
                                null,
                                vencimientoPeriodoActual,
                                estado);
        }
}