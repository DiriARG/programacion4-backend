package com.ironempire.mapper;

import com.ironempire.dto.response.fichamedica.FichaMedicaHistorialResponse;
import com.ironempire.dto.response.fichamedica.FichaMedicaResponse;
import com.ironempire.enums.EstadoFichaMedica;
import com.ironempire.model.FichaMedica;

import java.time.LocalDate;

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

    public FichaMedicaHistorialResponse convertirAHistorialResponse(FichaMedica fichaMedica,
            LocalDate fechaReferencia) {

        FichaMedicaHistorialResponse response = new FichaMedicaHistorialResponse();

        response.setId(fichaMedica.getId());

        response.setAlumnoId(fichaMedica.getAlumno().getId());
        response.setAlumnoNombre(fichaMedica.getAlumno().getNombre());
        response.setAlumnoApellido(fichaMedica.getAlumno().getApellido());
        response.setAlumnoDni(fichaMedica.getAlumno().getDni());

        response.setFechaActualizacion(fichaMedica.getFechaActualizacion());
        response.setFechaVencimiento(fichaMedica.getFechaVencimiento());

        response.setObservaciones(fichaMedica.getObservaciones());

        EstadoFichaMedica estado = fichaMedica.getFechaVencimiento()
                .isBefore(fechaReferencia)
                        ? EstadoFichaMedica.VENCIDA
                        : EstadoFichaMedica.VIGENTE;

        response.setEstado(estado);

        return response;
    }
}