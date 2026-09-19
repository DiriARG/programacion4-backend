package com.ironempire.dto.request.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearPlanRequest {

    @NotBlank(message = "El nombre del plan es obligatorio")
    @Size(max = 50, message = "El nombre del plan no puede superar los 50 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción del plan es obligatoria")
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String descripcion;

    @NotNull(message = "El precio mensual es obligatorio")
    @Positive(message = "El precio mensual debe ser mayor a cero")
    @Digits(integer = 8, fraction = 2, message = "El precio mensual debe tener como máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal precioMensual;

    // Se valida que la lista no sea null, no esté vacia y cada beneficio tenga texto real y no solamente espacios.
    @NotEmpty(message = "El plan debe tener al menos un beneficio")
    private List<@NotBlank(message = "El beneficio no puede estar vacío") String> beneficios;
}