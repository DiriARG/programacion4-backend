package com.ironempire.dto.response.pago;

import com.ironempire.enums.EstadoCuota;
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
public class CuotaActualResponse {
    private boolean planVigente;

    private String planNombre;
    private BigDecimal precioMensual;
    private BigDecimal montoAbonado;
    private LocalDate fechaPago;
    private LocalDate fechaVencimiento;
    private EstadoCuota estado;

    public static CuotaActualResponse sinPlanVigente() {
      return new CuotaActualResponse(false,null,null,null,null,null, null);
    }

}
