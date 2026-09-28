package com.ironempire.controller.pago;

import com.ironempire.dto.request.pago.RegistrarPagoRequest;
import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.service.pago.RegistrarPagoService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframwork.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class RegistrarPagoController {

    private final RegistrarPagoService registrarPagoService;

    // RN-68/RN-72 solo ADMIN_GESTION y ADMIN_GENERAL
    @PostMapping
    @PreAuthorize("hasAnYRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<PagoResponse> registrarPagoService(@Valid @RequestBody RegistrarPagoRequest request) {
        PagoResponse response = registrarPagoSerice.registrarPago(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

}
