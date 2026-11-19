package com.ironempire.service.notificacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ironempire.service.pago.EnviarReciboService;

import lombok.RequiredArgsConstructor;

/**
 * Envío automático del recibo (CU-AG-03, paso 11 y E9).
 *
 * AFTER_COMMIT: solo corre si el pago quedó confirmado en la base.
 * @Async: corre en otro hilo, así el admin no espera al servidor de correo.
 * Si falla, solo se loguea: el pago ya está guardado y no se revierte.
 *
 * Reutiliza EnviarReciboService, que carga alumno y plan con join fetch
 * (evita LazyInitializationException en el hilo asíncrono) y usa PlantillasMail.
 */
@Service
@RequiredArgsConstructor
public class EnviarReciboPagoService {

    private static final Logger log = LoggerFactory.getLogger(EnviarReciboPagoService.class);

    private final EnviarReciboService enviarReciboService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alRegistrarsePago(PagoRegistradoEvent event) {
        try {
            enviarReciboService.enviarRecibo(event.pagoId());
        } catch (Exception e) {
            log.error("No se pudo enviar el recibo del pago id={}", event.pagoId(), e);
        }
    }
}