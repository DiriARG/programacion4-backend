package com.ironempire.dto.response.turno;

import com.ironempire.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoResponse {

    private Long id;
    private String nombreClase;
    private Long profesorId;
    private String profesorNombre;
    private String profesorApellido;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean activo;
    private List<AlumnoTurnoResponse> alumnos;
}