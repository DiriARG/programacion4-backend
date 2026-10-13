package com.ironempire.controller.pago;

import com.ironempire.service.pago.EnviarReciboService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class EnviarReciboController {

    private final EnviarReciboService enviarReciboService;

    // Se llama después de registrar el pago, si el admin elige enviar el recibo. También sirve para reenviarlo.
    @PostMapping("/{id}/enviar-recibo")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<Void> enviarRecibo(@PathVariable Long id) {
        enviarReciboService.enviarRecibo(id);
        return ResponseEntity.noContent().build();
    }
}
