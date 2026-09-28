package com.ironempire.mapper;

import org.springframework.stereotype.Component;

import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.model.Pago;

@Component
public class PagoMapper {
    PagoResponse convertirAResponse(Pago pago) {

        PagoResponse response = new PagoResponse();

        response.setId(pago.getId());
        response.setPlanNombre(pago.getPlan().getNombre());
        response.setMontoAbonado(pago.getMontoAbonado());
        response.setFechaPago(pago.getFechaPago());
        response.setFechaVencimiento(pago.getFechaVencimiento());
        response.setMetodoPago(pago.getMetodoPago());
        response.setComprobanteKeyMinio(pago.getComprobanteKeyMinio());
        return response;
    }

}
