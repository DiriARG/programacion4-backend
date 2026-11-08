package com.ironempire.service.pago;

import org.springframework.stereotype.Service;

import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.model.Pago;
import com.ironempire.repository.JpaPagoRepository;
import com.ironempire.service.notificacion.MailService;
import com.ironempire.service.notificacion.PlantillasMail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnviarReciboService {

    private final JpaPagoRepository jpaPagoRepository;
    private final MailService mailService;
    private final PlantillasMail plantillasMail;

    /*
     * Sin @Transactional a propósito: findConDetalleById ya trae alumno y plan,
     * así no se mantiene una conexión a la base abierta mientras se espera al servidor de correo.
     */
    public void enviarRecibo(Long pagoId) {

        Pago pago = jpaPagoRepository.findConDetalleById(pagoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el pago indicado"));

        if (pago.isAnulado()) {
            throw new RecursoInvalidoException("No se puede enviar el recibo de un pago anulado.");
        }

        PlantillasMail.Contenido contenido = plantillasMail.recibo(pago);
        mailService.enviarHtml(pago.getAlumno().getEmail(), contenido.asunto(), contenido.html());
    }
}
