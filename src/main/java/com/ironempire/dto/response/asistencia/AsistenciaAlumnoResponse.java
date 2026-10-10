package com.ironempire.dto.response.asistencia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaAlumnoResponse {

    private LocalDate fecha;
    private LocalTime hora;
    private String nombreClase;
    private String registradoPorNombre;
    private String registradoPorApellido;
}