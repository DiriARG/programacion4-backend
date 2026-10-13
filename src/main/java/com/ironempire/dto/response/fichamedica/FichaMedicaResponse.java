package com.ironempire.dto.response.fichamedica;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FichaMedicaResponse {

    private Long id;
    private Long alumnoId;
    private String alumnoNombre;
    private String alumnoApellido;
    private String observaciones;
    private LocalDateTime fechaActualizacion;
    private LocalDate fechaVencimiento;
}