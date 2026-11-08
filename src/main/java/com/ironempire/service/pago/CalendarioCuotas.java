package com.ironempire.service.pago;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

/**
 * Único lugar donde se definen las fechas de una cuota.
 *
 * - Período: mes que se está pagando (puede abonarse por adelantado).
 * - Plazo de pago: día 10 del mes del período. Pasado ese día, sin pago, la cuota está VENCIDA.
 * - Fecha de vencimiento (la que se guarda en Pago): último día del mes del período.
 *
 * Registro, consulta de cuota y avisos usan esta clase para no desfasarse.
 */
@Component
public class CalendarioCuotas {

    public static final int DIA_LIMITE_PAGO = 10;
    private static final ZoneId ZONA = ZoneId.of("America/Argentina/Buenos_Aires");

    public LocalDate hoy() {
        return LocalDate.now(ZONA);
    }

    public LocalDateTime ahora() {
        return LocalDateTime.now(ZONA);
    }

    public YearMonth periodoActual() {
        return YearMonth.from(hoy());
    }

    public LocalDate vencimientoDe(YearMonth periodo) {
        return periodo.atEndOfMonth();
    }

    public LocalDate limitePagoDe(YearMonth periodo) {
        return periodo.atDay(DIA_LIMITE_PAGO);
    }
}
