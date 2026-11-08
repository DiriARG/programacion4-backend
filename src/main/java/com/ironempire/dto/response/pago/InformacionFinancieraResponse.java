package com.ironempire.dto.response.pago;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InformacionFinancieraResponse {
    private LocalDate desde;
    private LocalDate hasta;
    private BigDecimal ingresosTotales;
    private long cantidadPagos;
    private Map<String, BigDecimal> ingresosPorPlan;

}
