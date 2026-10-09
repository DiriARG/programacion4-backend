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
public class AsistenciaResponse {

    private Long id;
    private Long alumnoId;
    private String alumnoNombre;
    private String alumnoApellido;
    private Long turnoId;
    private String nombreClase;
    private LocalDate fecha;
    private LocalTime hora;
    private Long registradoPorId;
    private String registradoPorNombre;
    private String registradoPorApellido;
}