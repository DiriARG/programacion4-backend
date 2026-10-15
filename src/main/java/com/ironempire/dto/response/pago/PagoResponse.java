package com.ironempire.dto.response.pago;

import com.ironempire.enums.MetodoPago;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {

    private Long id;

    private Long alumnoId;
    private String alumnoNombre;
    private String alumnoApellido;

    private Long planId;
    private String planNombre;

    private BigDecimal montoAbonado;
    private LocalDate fechaPago;
    private LocalDate fechaVencimiento;
    private MetodoPago metodoPago;

    private boolean comprobanteAdjunto;
}