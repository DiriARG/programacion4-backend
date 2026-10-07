package com.ironempire.mapper;

import com.ironempire.dto.response.turno.AlumnoInscriptoResponse;
import com.ironempire.dto.response.turno.TurnoListadoResponse;
import com.ironempire.dto.response.turno.TurnoPropioAlumnoResponse;
import com.ironempire.dto.response.turno.TurnoPropioProfesorResponse;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.model.Turno;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TurnoMapper {

        // Para la consulta específica (detalle de cada turno).
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

        // Para el listado general.
        public TurnoListadoResponse convertirAListadoResponse(Turno turno) {

                TurnoListadoResponse response = new TurnoListadoResponse();

                response.setId(turno.getId());
                response.setNombreClase(turno.getNombreClase());
                response.setProfesorId(turno.getProfesor().getId());
                response.setProfesorNombre(turno.getProfesor().getNombre());
                response.setProfesorApellido(turno.getProfesor().getApellido());
                response.setDiaSemana(turno.getDiaSemana());
                response.setHoraInicio(turno.getHoraInicio());
                response.setHoraFin(turno.getHoraFin());
                response.setActivo(turno.getActivo());

                return response;
        }

        public TurnoPropioProfesorResponse convertirATurnoPropioProfesorResponse(Turno turno) {

                TurnoPropioProfesorResponse response = new TurnoPropioProfesorResponse();

                response.setId(turno.getId());
                response.setNombreClase(turno.getNombreClase());
                response.setDiaSemana(turno.getDiaSemana());
                response.setHoraInicio(turno.getHoraInicio());
                response.setHoraFin(turno.getHoraFin());
                response.setActivo(turno.getActivo());

                return response;
        }

        public TurnoPropioAlumnoResponse convertirATurnoPropioAlumnoResponse(Turno turno) {

                TurnoPropioAlumnoResponse response = new TurnoPropioAlumnoResponse();

                response.setId(turno.getId());
                response.setNombreClase(turno.getNombreClase());
                response.setDiaSemana(turno.getDiaSemana());
                response.setHoraInicio(turno.getHoraInicio());
                response.setHoraFin(turno.getHoraFin());
                response.setProfesorNombre(turno.getProfesor().getNombre());
                response.setProfesorApellido(turno.getProfesor().getApellido());

                return response;
        }
}