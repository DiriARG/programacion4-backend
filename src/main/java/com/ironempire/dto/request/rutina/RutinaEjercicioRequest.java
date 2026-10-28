package com.ironempire.dto.request.rutina;

import com.ironempire.enums.DiaSemana;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RutinaEjercicioRequest {

    @NotNull(message = "El ejercicio es obligatorio.")
    private Long ejercicioId;

    @NotNull(message = "La cantidad de series es obligatoria.")
    @Positive(message = "La cantidad de series debe ser mayor que cero.")
    private Integer series;

    @NotNull(message = "La cantidad de repeticiones es obligatoria.")
    @Positive(message = "La cantidad de repeticiones debe ser mayor que cero.")
    private Integer repeticiones;

    @DecimalMin(value = "0.00", message = "El peso no puede ser negativo.")
    @Digits(integer = 3, fraction = 2, message = "El peso debe tener como máximo 3 enteros y 2 decimales.")
    private BigDecimal peso;

    @NotNull(message = "El día de la semana es obligatorio.")
    private DiaSemana diaSemana;

    @NotNull(message = "El orden es obligatorio.")
    @Positive(message = "El orden debe ser mayor que cero.")
    private Integer orden;
}