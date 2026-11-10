package com.ironempire.mapper;

import org.springframework.stereotype.Component;

import com.ironempire.dto.response.ejercicio.EjercicioResponse;
import com.ironempire.model.Ejercicio;

@Component
public class EjercicioMapper {

    public EjercicioResponse convertirAResponse(Ejercicio ejercicio) {

        EjercicioResponse response = new EjercicioResponse();

        response.setId(ejercicio.getId());
        response.setNombre(ejercicio.getNombre());
        response.setGrupoMuscular(ejercicio.getGrupoMuscular());
        response.setDescripcion(ejercicio.getDescripcion());
        response.setActivo(ejercicio.getActivo());

        if (ejercicio.getImagenKeyMinio() != null
                && !ejercicio.getImagenKeyMinio().isBlank()) {

            response.setImagenUrl(
                    "/api/ejercicios/" + ejercicio.getId() + "/imagen");
        }

        return response;
    }
}