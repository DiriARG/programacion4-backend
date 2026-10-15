package com.ironempire.dto.request.pago;

import com.ironempire.enums.MetodoPago;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarPagoRequest {

    @NotNull(message = "El alumno es obligatorio")
    private Long alumnoId;

    @NotNull(message = "El plan es obligatorio")
    private Long planId;

    @NotNull(message = "El monto abonado es obligatorio")

    @DecimalMin(value = "0.01", message = "El monto abonado debe ser como mínimo 0,01")
    @Digits(integer = 8, fraction = 2, message = "El monto abonado debe tener como máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal montoAbonado;

    @NotNull(message = "La fecha de pago es obligatoria")
    /*
     * Se utiliza @DateTimeFormat porque al ser una petición multipart/form-data
     * (por incluir el comprobante), Spring procesa los campos como formulario
     * y no asume el estándar ISO por defecto (a diferencia del formato JSON).
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaPago;

    @NotNull(message = "El período de la cuota es obligatorio")
    // Recibe el período como año-mes: uuuu-MM, por ejemplo 2026-08.
    @DateTimeFormat(pattern = "uuuu-MM")
    private YearMonth periodo;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    private MultipartFile comprobante;
}
