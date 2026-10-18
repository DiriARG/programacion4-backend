package com.ironempire.mapper;

import com.ironempire.dto.response.asistencia.AsistenciaAlumnoResponse;
import com.ironempire.dto.response.asistencia.AsistenciaHistorialResponse;
import com.ironempire.dto.response.asistencia.AsistenciaResponse;
import com.ironempire.model.Asistencia;

import org.springframework.stereotype.Component;

@Component
public class AsistenciaMapper {

    public AsistenciaResponse convertirAResponse(Asistencia asistencia) {

        AsistenciaResponse response = new AsistenciaResponse();

        response.setId(asistencia.getId());
        response.setAlumnoId(asistencia.getAlumno().getId());
        response.setAlumnoNombre(asistencia.getAlumno().getNombre());
        response.setAlumnoApellido(asistencia.getAlumno().getApellido());
        
        if (asistencia.getTurno() != null) {
            response.setTurnoId(asistencia.getTurno().getId());
            response.setNombreClase(asistencia.getTurno().getNombreClase());
        }

        response.setFecha(asistencia.getFecha());
        response.setHora(asistencia.getHora());
        response.setRegistradoPorId(asistencia.getRegistradoPor().getId());
        response.setRegistradoPorNombre(asistencia.getRegistradoPor().getNombre());
        response.setRegistradoPorApellido(asistencia.getRegistradoPor().getApellido());

        return response;
    }

    public AsistenciaAlumnoResponse convertirAAsistenciaAlumnoResponse(Asistencia asistencia) {

        AsistenciaAlumnoResponse response = new AsistenciaAlumnoResponse();

        response.setFecha(asistencia.getFecha());
        response.setHora(asistencia.getHora());

        if (asistencia.getTurno() != null) {
            response.setNombreClase(asistencia.getTurno().getNombreClase());
        }

        response.setRegistradoPorNombre(asistencia.getRegistradoPor().getNombre());
        response.setRegistradoPorApellido(asistencia.getRegistradoPor().getApellido());

        return response;
    }

    public AsistenciaHistorialResponse convertirAHistorialResponse(Asistencia asistencia) {

        AsistenciaHistorialResponse response = new AsistenciaHistorialResponse();

        response.setId(asistencia.getId());
        response.setAlumnoId(asistencia.getAlumno().getId());
        response.setAlumnoNombre(asistencia.getAlumno().getNombre());
        response.setAlumnoApellido(asistencia.getAlumno().getApellido());
        response.setAlumnoDni(asistencia.getAlumno().getDni());

        if (asistencia.getTurno() != null) {
            response.setTurnoId(asistencia.getTurno().getId());
            response.setNombreClase(asistencia.getTurno().getNombreClase());
        }

        response.setFecha(asistencia.getFecha());
        response.setHora(asistencia.getHora());
        response.setRegistradoPorId(asistencia.getRegistradoPor().getId());
        response.setRegistradoPorNombre(asistencia.getRegistradoPor().getNombre());
        response.setRegistradoPorApellido(asistencia.getRegistradoPor().getApellido());
        response.setRegistradoPorRol(asistencia.getRegistradoPor().getRol());

        return response;
    }
}