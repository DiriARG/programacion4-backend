package com.ironempire.controller.pago;

import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.service.pago.ConsultarHistorialPagosService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class ConsultarHistorialPagosController {

    private final ConsultarHistorialPagosService consultarHistorialPagosService;

    // RN-57: el alumno consulta unicamente su propio historial de pagos
    @GetMapping("/mi-historial")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<List<PagoResponse>> consultarMiHistorial(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(consultarHistorialPagosService.consultarHistorialPagos(email));

    }

}
