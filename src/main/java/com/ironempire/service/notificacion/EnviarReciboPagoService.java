package com.ironempire.service.notificacion;

import com.ironempire.model.Pago;
import com.ironempire.repository.JpaPagoRepository;

import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EnviarReciboPagoService {

     private static final Logger log = LoggerFactory.getLogger(EnviarReciboPagoService.class);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JpaPagoRepository pagoRepository;
    private final JavaMailSender mailSender;

    @Value("${gym.mail.remitente}")
    private String remitente;

    // AFTER_COMMIT: solo se ejecuta si la transacción de RegistrarPagoService
    // confirmó con éxito. @Async: corre en otro hilo, así el admin no espera
    // al envío del mail. Si falla, solo se loguea: el pago ya quedó guardado.
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alRegistrarsePago(PagoRegistradoEvent event) {
        try {
            enviarRecibo(event.pagoId());
        } catch (Exception e) {
            log.error("No se pudo enviar el recibo del pago id={}", event.pagoId(), e);
        }
    }

    private void enviarRecibo(Long pagoId) throws Exception {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new IllegalStateException(
                        "Pago id=" + pagoId + " no encontrado al intentar enviar el recibo."));

        String html = construirHtmlRecibo(pago);

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, false, "UTF-8");
        helper.setFrom(remitente);
        helper.setTo(pago.getAlumno().getEmail());
        helper.setSubject("Recibo de pago - Iron Empire Gym");
        helper.setText(html, true);

        mailSender.send(mensaje);
    }

    private String construirHtmlRecibo(Pago pago) {
        return """
                <html>
                  <body style="font-family: Arial, sans-serif; color: #222;">
                    <h2>Iron Empire Gym</h2>
                    <p>Hola %s, registramos tu pago con éxito.</p>
                    <table style="border-collapse: collapse; margin-top: 16px;">
                      <tr><td style="padding:6px 12px;font-weight:bold;">Plan</td><td style="padding:6px 12px;">%s</td></tr>
                      <tr><td style="padding:6px 12px;font-weight:bold;">Monto abonado</td><td style="padding:6px 12px;">$ %s</td></tr>
                      <tr><td style="padding:6px 12px;font-weight:bold;">Método de pago</td><td style="padding:6px 12px;">%s</td></tr>
                      <tr><td style="padding:6px 12px;font-weight:bold;">Fecha de pago</td><td style="padding:6px 12px;">%s</td></tr>
                      <tr><td style="padding:6px 12px;font-weight:bold;">Vencimiento del período</td><td style="padding:6px 12px;">%s</td></tr>
                    </table>
                    <p style="margin-top:24px;font-size:12px;color:#777;">
                      Este es un comprobante generado automáticamente, no responder a este email.
                    </p>
                  </body>
                </html>
                """.formatted(
                pago.getAlumno().getNombre(),
                pago.getPlan().getNombre(),
                pago.getMontoAbonado(),
                pago.getMetodoPago(),
                pago.getFechaPago().format(FORMATO_FECHA),
                pago.getFechaVencimiento().format(FORMATO_FECHA)
        );
    }
}
