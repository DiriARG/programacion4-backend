package com.ironempire.mapper;

import com.ironempire.dto.response.fichamedica.FichaMedicaResponse;
import com.ironempire.model.FichaMedica;

import org.springframework.stereotype.Component;

@Component
public class FichaMedicaMapper {

    public FichaMedicaResponse convertirAResponse(FichaMedica fichaMedica) {

        FichaMedicaResponse response = new FichaMedicaResponse();

        response.setId(fichaMedica.getId());
        response.setAlumnoId(fichaMedica.getAlumno().getId());
        response.setAlumnoNombre(fichaMedica.getAlumno().getNombre());
        response.setAlumnoApellido(fichaMedica.getAlumno().getApellido());
        response.setObservaciones(fichaMedica.getObservaciones());
        response.setFechaActualizacion(fichaMedica.getFechaActualizacion());
        response.setFechaVencimiento(fichaMedica.getFechaVencimiento());

        return response;
    }
}