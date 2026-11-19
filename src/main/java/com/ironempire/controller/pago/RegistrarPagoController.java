package com.ironempire.controller.pago;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ironempire.dto.request.pago.RegistrarPagoRequest;
import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.service.pago.RegistrarPagoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class RegistrarPagoController {

    private final RegistrarPagoService registrarPagoService;

    // solo ADMIN_GESTION y ADMIN_GENERAL.
    // El frontend envía multipart: parte "pago" (JSON) y parte "comprobante" (opcional).
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<PagoResponse> registrarPago(
            @Valid @RequestPart("pago") RegistrarPagoRequest request,
            @RequestPart(value = "comprobante", required = false) MultipartFile comprobante) {

        PagoResponse response = registrarPagoService.registrarPago(request, comprobante);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}