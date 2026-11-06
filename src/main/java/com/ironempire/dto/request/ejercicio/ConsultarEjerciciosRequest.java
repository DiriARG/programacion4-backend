package com.ironempire.dto.request.ejercicio;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarEjerciciosRequest {

    /* Es "Boolean" y no "boolean" (primitivo) ya que este primero puede tomar los valores:
    true --> consultar todos los ejercicios activos
    false --> consultar todos los ejercicios inactivos
    null --> consultar todos los ejercicios.*/
    private Boolean activo;

    private String busqueda;

    @Min(value = 0, message = "La página no puede ser negativa.")
    private int page = 0;

    @Min(value = 1, message = "El tamaño de página debe ser como mínimo 1.")
    @Max(value = 100, message = "El tamaño de página no puede superar los 100 registros.")
    private int size = 10;
}