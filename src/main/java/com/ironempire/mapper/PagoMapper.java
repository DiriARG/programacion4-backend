package com.ironempire.mapper;

import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.model.Pago;

import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public PagoResponse convertirAResponse(Pago pago) {

        PagoResponse response = new PagoResponse();

        response.setId(pago.getId());

        response.setAlumnoId(pago.getAlumno().getId());
        response.setAlumnoNombre(pago.getAlumno().getNombre());
        response.setAlumnoApellido(pago.getAlumno().getApellido());

        response.setPlanId(pago.getPlan().getId());
        response.setPlanNombre(pago.getPlan().getNombre());

        response.setMontoAbonado(pago.getMontoAbonado());
        response.setFechaPago(pago.getFechaPago());
        response.setFechaVencimiento(pago.getFechaVencimiento());
        response.setMetodoPago(pago.getMetodoPago());

        response.setComprobanteAdjunto(
                pago.getComprobanteKeyMinio() != null
                        && !pago.getComprobanteKeyMinio().isBlank());

        return response;
    }
}