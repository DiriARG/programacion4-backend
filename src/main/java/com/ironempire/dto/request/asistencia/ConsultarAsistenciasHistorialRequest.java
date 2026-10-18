package com.ironempire.dto.request.asistencia;

import com.ironempire.enums.ModalidadAsistencia;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/*
 * Este DTO recibe los filtros opcionales y los parámetros de paginación
 * enviados en la URL.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarAsistenciasHistorialRequest {

    /*
     * En este caso es necesario el @DateTimeFormat porque este LocalDate va a
     * viajar en un GET como parámetros en la url.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaDesde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaHasta;

    private String alumno;

    // Si no se especifica, se consultan todas las modalidades.
    private ModalidadAsistencia modalidad = ModalidadAsistencia.TODAS;

    private Long turnoId;

    // La numeración de las páginas comienza en cero.
    @Min(value = 0, message = "La página no puede ser negativa.")
    private int page = 0;

    // El tamaño predeterminado es 10 y el máximo permitido es 100.
    @Min(value = 1, message = "El tamaño de página debe ser como mínimo 1.")
    @Max(value = 100, message = "El tamaño de página no puede superar los 100 registros.")
    private int size = 10;
}