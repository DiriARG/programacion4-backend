package com.ironempire.mapper;

import org.springframework.stereotype.Component;

import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.model.Pago;

@Component
public class PagoMapper {
    public PagoResponse convertirAResponse(Pago pago) {

        PagoResponse response = new PagoResponse();

        response.setId(pago.getId());
        
        // Datos del plan
        response.setPlanNombre(pago.getPlan().getNombre());
        
        // Datos del alumno (Asegúrate de que la entidad Pago tenga la relación con Usuario/Alumno)
        if (pago.getAlumno() != null) {
            response.setNombreAlumno(pago.getAlumno().getNombre());
            response.setApellidoAlumno(pago.getAlumno().getApellido());
            response.setDniAlumno(pago.getAlumno().getDni());
        }

        // Período
        response.setMesPeriodo(pago.getMesPeriodo());
        response.setAnioPeriodo(pago.getAnioPeriodo());

        response.setMontoAbonado(pago.getMontoAbonado());
        response.setFechaPago(pago.getFechaPago());
        response.setFechaVencimiento(pago.getFechaVencimiento());
        response.setMetodoPago(pago.getMetodoPago());
        response.setComprobanteKeyMinio(pago.getComprobanteKeyMinio());
        response.setAnulado(pago.isAnulado());
        
        return response;
    }
}