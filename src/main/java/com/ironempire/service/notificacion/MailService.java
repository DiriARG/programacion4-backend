package com.ironempire.service.notificacion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ironempire.exception.MailEnvioException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Envío de correos. Es síncrono: quien lo llama se entera si falló.
 * Si falla, lanza MailEnvioException (el GlobalExceptionHandler la traduce a 502).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${gym.mail.remitente}")
    private String remitente;

    public void enviarHtml(String destinatario, String asunto, String cuerpoHtml) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, "UTF-8");
            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoHtml, true);
            mailSender.send(mensaje);
        } catch (MessagingException | MailException e) {
            log.error("Error al enviar correo a {}", destinatario, e);
            throw new MailEnvioException("No se pudo enviar el correo a " + destinatario, e);
        }
    }
}
