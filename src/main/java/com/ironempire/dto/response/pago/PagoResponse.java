package com.ironempire.dto.response.pago;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {

    private Long id;
    private String planNombre;
    
    // Datos del alumno para la tabla y el modal de anulación
    private String nombreAlumno;
    private String apellidoAlumno;
    private String dniAlumno;

    // Período
    private Integer mesPeriodo;
    private Integer anioPeriodo;

    private BigDecimal montoAbonado;
    private LocalDate fechaPago;
    private LocalDate fechaVencimiento;
    private String metodoPago;
    private String comprobanteKeyMinio;
    private boolean anulado;

}