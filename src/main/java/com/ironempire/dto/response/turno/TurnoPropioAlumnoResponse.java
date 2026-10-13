package com.ironempire.dto.response.turno;

import java.time.LocalTime;

import com.ironempire.enums.DiaSemana;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoPropioAlumnoResponse {

    private Long id;
    private String nombreClase;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String profesorNombre;
    private String profesorApellido;
}