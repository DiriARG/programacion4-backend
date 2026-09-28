package com.ironempire.dto.request.pago;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarPagoRequest {

    @NotNull (message = "El alumno es obligatorio")
    private Long alumnoId;

    @NotNull (message = "El plan es obligatorio")
    private Long planId;

    @NotNull (message = "El monto abonado es obligatorio")
    @Positive (message = "El monto abonado debe ser mayor a cero")
    @Digits (integer = 8, fraction = 2, message = "El monto  debe tener como máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal montoAbonado;

    @NotNull (message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;

    @NotNull (message = "El mes del período es obligatorio")
    @Min(value = 1, message = "El mes del período debe estar entre 1 y 12")
    @Max(value = 12, message = "El mes del período debe estar entre 1 y 12")
    private Integer mesPeriodo;

    @NotNull(message = "El año del período es obligatorio")
    private Integer anioPeriodo;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 50, message = "El método de pago no puede tener más de 50 caracteres")
    private String metodoPago;
    private String comprobanteKeyMinio;

}
