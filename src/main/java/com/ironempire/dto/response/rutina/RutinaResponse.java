package com.ironempire.dto.response.rutina;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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