package com.ironempire.dto.response.fichamedica;

import com.ironempire.enums.EstadoFichaMedica;
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
public class FichaMedicaHistorialResponse {

    private Long id;

    private Long alumnoId;
    private String alumnoNombre;
    private String alumnoApellido;
    private String alumnoDni;

    private LocalDateTime fechaActualizacion;
    private LocalDate fechaVencimiento;

    private String observaciones;

    private EstadoFichaMedica estado;
}