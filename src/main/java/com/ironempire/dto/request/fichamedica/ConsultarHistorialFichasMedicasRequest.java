package com.ironempire.dto.request.fichamedica;

import com.ironempire.enums.EstadoFichaMedica;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarHistorialFichasMedicasRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaVencimientoDesde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaVencimientoHasta;

    private String alumno;

    private EstadoFichaMedica estado;

    @Min(value = 0, message = "La página no puede ser negativa.")
    private int page = 0;

    @Min(value = 1, message = "El tamaño de página debe ser como mínimo 1.")
    @Max(value = 100, message = "El tamaño de página no puede superar los 100 registros.")
    private int size = 10;
}