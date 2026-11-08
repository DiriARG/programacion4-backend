package com.ironempire.service.notificacion;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import com.ironempire.model.Pago;
import com.ironempire.model.Usuario;

/**
 * Arma el asunto y el HTML de cada correo. Todo dato variable se escapa
 * para que un nombre con caracteres raros no rompa (ni inyecte) HTML.
 */
@Component
public class PlantillasMail {

    public record Contenido(String asunto, String html) {
    }

    private static final Locale AR = Locale.forLanguageTag("es-AR");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String GIMNASIO = "Iron Empire Gym";

    public Contenido recibo(Pago pago) {
        Usuario alumno = pago.getAlumno();
        YearMonth periodo = YearMonth.from(pago.getFechaVencimiento());
        String nombrePeriodo = nombrePeriodo(periodo);

        String filas = fila("N° de recibo", String.valueOf(pago.getId()))
                + fila("Plan", pago.getPlan().getNombre())
                + fila("Período abonado", nombrePeriodo)
                + fila("Monto abonado", NumberFormat.getCurrencyInstance(AR).format(pago.getMontoAbonado()))
                + fila("Fecha de pago", pago.getFechaPago().format(FECHA))
                + fila("Método de pago", pago.getMetodoPago())
                + fila("Cubre hasta", pago.getFechaVencimiento().format(FECHA));

        String cuerpo = "<p>Hola " + h(alumno.getNombre()) + ",</p>"
                + "<p>Registramos tu pago. Estos son los datos de tu recibo:</p>"
                + "<table style=\"border-collapse:collapse;background:#f6f6f6\">" + filas + "</table>"
                + "<p>¡Gracias por entrenar con nosotros!</p>";

        return new Contenido("Recibo de pago - " + nombrePeriodo, envolver(cuerpo));
    }

    public Contenido avisoCuotaPendiente(Usuario alumno, YearMonth periodo, LocalDate limitePago) {
        String nombrePeriodo = nombrePeriodo(periodo);

        String cuerpo = "<p>Hola " + h(alumno.getNombre()) + ",</p>"
                + "<p>Notamos que tu cuota de <strong>" + h(nombrePeriodo) + "</strong> todavía no figura abonada. "
                + "El plazo para pagarla venció el " + limitePago.format(FECHA) + ".</p>"
                + "<p>Podés acercarte a recepción para regularizarla.</p>"
                + "<p>Si ya la abonaste, avisanos así lo revisamos.</p>";

        return new Contenido("Cuota pendiente - " + nombrePeriodo, envolver(cuerpo));
    }

    private String envolver(String cuerpo) {
        return "<div style=\"font-family:Arial,sans-serif;max-width:520px;margin:auto\">"
                + "<h2 style=\"margin-bottom:4px\">" + h(GIMNASIO) + "</h2><hr>"
                + cuerpo
                + "<hr><p style=\"color:#888;font-size:12px\">Este es un mensaje automático, por favor no lo respondas.</p>"
                + "</div>";
    }

    private String fila(String etiqueta, String valor) {
        return "<tr><td style=\"padding:6px 12px;color:#555\">" + h(etiqueta) + "</td>"
                + "<td style=\"padding:6px 12px\"><strong>" + h(valor) + "</strong></td></tr>";
    }

    private String nombrePeriodo(YearMonth periodo) {
        String mes = periodo.getMonth().getDisplayName(TextStyle.FULL, AR);
        return Character.toUpperCase(mes.charAt(0)) + mes.substring(1) + " " + periodo.getYear();
    }

    private static String h(String texto) {
        return HtmlUtils.htmlEscape(texto == null ? "" : texto);
    }
}
