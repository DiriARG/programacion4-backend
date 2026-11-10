package com.ironempire.dto.response.rutina;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutinaListadoResponse {

    private Long id;

    private String alumnoNombre;
    private String alumnoApellido;
    private String alumnoDni;

    private String nombre;
    private LocalDate fechaCreacion;
    private Boolean activa;
}