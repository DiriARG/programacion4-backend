package com.ironempire.controller.ejercicio;

import com.ironempire.dto.request.ejercicio.ConsultarEjerciciosRequest;
import com.ironempire.dto.response.ejercicio.EjercicioListadoPageResponse;
import com.ironempire.service.ejercicio.ConsultarEjerciciosService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ejercicios")
@RequiredArgsConstructor
public class ConsultarEjerciciosController {

    private final ConsultarEjerciciosService consultarEjerciciosService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<EjercicioListadoPageResponse> consultarEjercicios(
            @Valid @ModelAttribute ConsultarEjerciciosRequest filtros) {

        EjercicioListadoPageResponse response = consultarEjerciciosService.consultarEjercicios(filtros);

        return ResponseEntity.ok(response);
    }
}