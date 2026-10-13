package com.ironempire.service.pago;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ironempire.dto.request.pago.RegistrarPagoRequest;
import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.PagoMapper;
import com.ironempire.model.Pago;
import com.ironempire.model.Plan;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaPagoRepository;
import com.ironempire.repository.JpaPlanRepository;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrarPagoService {

    private final JpaPagoRepository jpaPagoRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final JpaPlanRepository jpaPlanRepository;
    private final PagoMapper pagoMapper;
    private final CalendarioCuotas calendario;

    @Transactional
    public PagoResponse registrarPago(RegistrarPagoRequest request) {

        // RN-18
        Usuario alumno = jpaUsuarioRepository.findById(request.getAlumnoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado"));

        if (alumno.getRol() != Rol.ALUMNO) {
            throw new RecursoInvalidoException("El usuario no es un ALUMNO");
        }

        // RN-19
        Plan plan = jpaPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el plan indicado"));

        // RN-17
        if (!Boolean.TRUE.equals(plan.getActivo())) {
            throw new RecursoInvalidoException(
                    "El plan seleccionado está inactivo y no puede utilizarse para registrar pagos.");
        }

        // RN-21: el frontend no manda la fecha de vencimiento; se calcula desde el período.
        YearMonth periodo = YearMonth.of(request.getAnioPeriodo(), request.getMesPeriodo());
        LocalDate fechaVencimiento = calendario.vencimientoDe(periodo);

        // RN-24: un pago vigente (no anulado) por alumno y período.
        if (jpaPagoRepository.existsByAlumnoIdAndFechaVencimientoAndAnuladoFalse(alumno.getId(), fechaVencimiento)) {
            throw new RecursoExistenteException("El alumno ya tiene un pago registrado para ese período.");
        }

        Pago pago = new Pago();
        pago.setAlumno(alumno);
        pago.setPlan(plan);
        pago.setMontoAbonado(request.getMontoAbonado());
        pago.setFechaPago(request.getFechaPago());
        pago.setFechaVencimiento(fechaVencimiento);
        pago.setMetodoPago(request.getMetodoPago());
        pago.setComprobanteKeyMinio(request.getComprobanteKeyMinio());
        
        return pagoMapper.convertirAResponse(jpaPagoRepository.save(pago));
    }
}
