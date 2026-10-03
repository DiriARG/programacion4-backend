package com.ironempire.mapper;

import com.ironempire.dto.response.turno.AlumnoInscriptoResponse;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.model.Turno;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TurnoMapper {

        public TurnoResponse convertirAResponse(
                        Turno turno,
                        List<AlumnoInscriptoResponse> alumnos) {

                TurnoResponse response = new TurnoResponse();

                response.setId(turno.getId());
                response.setNombreClase(turno.getNombreClase());
                response.setProfesorId(turno.getProfesor().getId());
                response.setProfesorNombre(turno.getProfesor().getNombre());
                response.setProfesorApellido(turno.getProfesor().getApellido());
                response.setDiaSemana(turno.getDiaSemana());
                response.setHoraInicio(turno.getHoraInicio());
                response.setHoraFin(turno.getHoraFin());
                response.setActivo(turno.getActivo());
                response.setAlumnos(alumnos);

                return response;
        }
}