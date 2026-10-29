package com.ironempire.mapper;

import com.ironempire.dto.response.rutina.RutinaEjercicioResponse;
import com.ironempire.dto.response.rutina.RutinaListadoResponse;
import com.ironempire.dto.response.rutina.RutinaResponse;
import com.ironempire.model.Ejercicio;
import com.ironempire.model.Rutina;
import com.ironempire.model.RutinaEjercicio;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RutinaMapper {

    public RutinaResponse convertirAResponse(
            Rutina rutina,
            List<RutinaEjercicio> ejercicios) {

        RutinaResponse response = new RutinaResponse();

        response.setId(rutina.getId());

        response.setAlumnoId(rutina.getAlumno().getId());
        response.setAlumnoNombre(rutina.getAlumno().getNombre());
        response.setAlumnoApellido(rutina.getAlumno().getApellido());

        response.setProfesorId(rutina.getProfesor().getId());
        response.setProfesorNombre(rutina.getProfesor().getNombre());
        response.setProfesorApellido(rutina.getProfesor().getApellido());

        response.setNombre(rutina.getNombre());
        response.setFechaCreacion(rutina.getFechaCreacion());
        response.setActiva(rutina.getActiva());

        response.setEjercicios(
                ejercicios.stream()
                        .map(this::convertirAEjercicioResponse)
                        .toList());

        return response;
    }

    public RutinaEjercicioResponse convertirAEjercicioResponse(
            RutinaEjercicio rutinaEjercicio) {

        /*
         * Obtiene el ejercicio del catálogo asociado para acceder a sus datos
         * generales.
         */
        Ejercicio ejercicio = rutinaEjercicio.getEjercicio();

        RutinaEjercicioResponse response = new RutinaEjercicioResponse();

        response.setId(rutinaEjercicio.getId());
        response.setEjercicioId(ejercicio.getId());
        response.setEjercicioNombre(ejercicio.getNombre());
        response.setGrupoMuscular(ejercicio.getGrupoMuscular());
        response.setDescripcion(ejercicio.getDescripcion());

        response.setSeries(rutinaEjercicio.getSeries());
        response.setRepeticiones(rutinaEjercicio.getRepeticiones());
        response.setPeso(rutinaEjercicio.getPeso());
        response.setDiaSemana(rutinaEjercicio.getDiaSemana());
        response.setOrden(rutinaEjercicio.getOrden());

        return response;
    }

    public RutinaListadoResponse convertirAListadoResponse(Rutina rutina) {

        RutinaListadoResponse response = new RutinaListadoResponse();

        response.setId(rutina.getId());

        response.setAlumnoNombre(rutina.getAlumno().getNombre());
        response.setAlumnoApellido(rutina.getAlumno().getApellido());
        response.setAlumnoDni(rutina.getAlumno().getDni());

        response.setNombre(rutina.getNombre());
        response.setFechaCreacion(rutina.getFechaCreacion());
        response.setActiva(rutina.getActiva());

        return response;
    }
}