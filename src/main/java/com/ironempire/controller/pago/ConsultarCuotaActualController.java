package com.ironempire.controller.pago;

import com.ironempire.dto.response.pago.CuotaActualResponse;
import com.ironempire.service.pago.ConsultarCuotaActualService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class ConsultarCuotaActualController {

    private final ConsultarCuotaActualService consultarCuotaActualService;

    // RN-56: el alumno consulta el estado y plan correspondiente a su cuota.
    @GetMapping("/mi-cuota-actual")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<CuotaActualResponse> consultarMiCuotaActual(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(consultarCuotaActualService.consultarCuotaActual(email));
    }

}
