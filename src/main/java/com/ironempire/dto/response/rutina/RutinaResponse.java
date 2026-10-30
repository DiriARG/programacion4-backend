package com.ironempire.dto.response.rutina;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutinaResponse {

    private Long id;

    private Long alumnoId;
    private String alumnoNombre;
    private String alumnoApellido;

    private Long profesorId;
    private String profesorNombre;
    private String profesorApellido;

    private String nombre;
    private LocalDate fechaCreacion;
    private Boolean activa;

    private List<RutinaEjercicioResponse> ejercicios;
}