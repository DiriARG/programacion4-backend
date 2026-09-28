package com.ironempire.controller.pago;

import com.ironempire.service.pago.ConsultarInformacionFinancieraService;
import com.ironempire.dto.response.pago.InformacionFinancieraResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class ConsultarInformacionFinancieraController {

    private final ConsultarInformacionFinancieraService consultarInformacionFinancieraService;
    @GetMapping("/financiero")
    @PreAuthoriz("hasRole('ADMIN__GENERAL')")
    public ResponseEntity<InformacionFinancieraResponse> consultarInformacionFinancieraService(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
    return ResponseEntity.ok(
        consultarInformacionFinancieraService.consultarInformacionFinanciera(desde, hasta));

        }
        
}
