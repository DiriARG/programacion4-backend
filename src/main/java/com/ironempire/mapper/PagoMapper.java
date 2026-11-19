package com.ironempire.mapper;

import org.springframework.stereotype.Component;

import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.model.Pago;
import java.time.YearMonth;


@Component
public class PagoMapper {
    public PagoResponse convertirAResponse(Pago pago) {

        PagoResponse response = new PagoResponse();

        response.setId(pago.getId());
               
        response.setPlanNombre(pago.getPlan().getNombre());        
        
        if (pago.getAlumno() != null) {
            response.setNombreAlumno(pago.getAlumno().getNombre());
            response.setApellidoAlumno(pago.getAlumno().getApellido());
            response.setDniAlumno(pago.getAlumno().getDni());
        }
        
        YearMonth periodo = YearMonth.from(pago.getFechaVencimiento());
        response.setMesPeriodo(periodo.getMonthValue());
        response.setAnioPeriodo(periodo.getYear());
        
        response.setMontoAbonado(pago.getMontoAbonado());
        response.setFechaPago(pago.getFechaPago());
        response.setFechaVencimiento(pago.getFechaVencimiento());
        response.setMetodoPago(pago.getMetodoPago());
        response.setComprobanteKeyMinio(pago.getComprobanteKeyMinio());
        response.setAnulado(pago.isAnulado());
        
        return response;
    }
}