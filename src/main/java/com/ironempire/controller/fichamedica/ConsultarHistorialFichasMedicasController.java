package com.ironempire.controller.fichamedica;

import com.ironempire.dto.request.fichamedica.ConsultarHistorialFichasMedicasRequest;
import com.ironempire.dto.response.fichamedica.FichaMedicaHistorialPageResponse;
import com.ironempire.service.fichamedica.ConsultarHistorialFichasMedicasService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fichas-medicas")
@RequiredArgsConstructor
public class ConsultarHistorialFichasMedicasController {

    private final ConsultarHistorialFichasMedicasService consultarHistorialFichasMedicasService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<FichaMedicaHistorialPageResponse> consultar(
            @Valid @ModelAttribute ConsultarHistorialFichasMedicasRequest filtros) {

        FichaMedicaHistorialPageResponse response = consultarHistorialFichasMedicasService.consultar(filtros);

        return ResponseEntity.ok(response);
    }
}