package com.ironempire.controller.pago;

import com.ironempire.dto.request.pago.AnularPagoRequest;
import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.service.pago.AnularPagoService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class AnularPagoController {

    private final AnularPagoService anularPagoService;

    // Solo ADMIN_GENERAL puede anular pagos.
    @PatchMapping("/{id}/anular")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<PagoResponse> anularPago(
            @PathVariable Long id,
            @Valid @RequestBody AnularPagoRequest request,
            Principal principal) {
        return ResponseEntity.ok(anularPagoService.anularPago(id, request, principal.getName()));
    }
}
