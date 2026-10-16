package com.ironempire.controller.pago;

import com.ironempire.dto.request.pago.RegistrarPagoRequest;
import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.service.pago.RegistrarPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class RegistrarPagoController {

    private final RegistrarPagoService registrarPagoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<PagoResponse> registrarPago(
            @Valid @ModelAttribute RegistrarPagoRequest request) {

        PagoResponse response = registrarPagoService.registrarPago(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}