package com.ironempire.dto.request.rutina;

import com.ironempire.enums.DiaSemana;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModificarRutinaEjercicioRequest {

    // Identifica un ejercicio que ya está incorporado a una rutina.
    @Positive(message = "El ID de la asociación debe ser mayor que cero.")
    private Long rutinaEjercicioId;

    // Identifica un ejercicio del catálogo que se quiere incorporar a la rutina.
    @Positive(message = "El ID del ejercicio debe ser mayor que cero.")
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

    /*
     * Uno de los dos identificadores se debe informar, osea cuando modificamos una
     * asociación existente no se envía "ejercicioId".
     * Tampoco se envía "rutinaEjercicioId" cuando se agrega un ejercicio nuevo a la
     * rutina.
     * 
     * @AssertTrue indica que el resultado de este método debe ser true para que el
     * objeto supere la validación.
     */
    @AssertTrue(message = "Debe informar exactamente uno de los identificadores: rutinaEjercicioId o ejercicioId.")
    public boolean isIdentificadorValido() {
        /*
         * Devuelve true solo cuando uno es null y false cuando los dos son null o ambos
         * tienen un valor.
         */
        return (rutinaEjercicioId == null) != (ejercicioId == null);
    }
}