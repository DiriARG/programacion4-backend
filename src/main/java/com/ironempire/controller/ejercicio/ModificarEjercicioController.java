package com.ironempire.controller.ejercicio;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ironempire.dto.request.ejercicio.ModificarEjercicioRequest;
import com.ironempire.dto.response.ejercicio.EjercicioResponse;
import com.ironempire.service.ejercicio.ModificarEjercicioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ejercicios")
@RequiredArgsConstructor
public class ModificarEjercicioController {

    private final ModificarEjercicioService modificarEjercicioService;

    @PutMapping(value = "/{ejercicioId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<EjercicioResponse> modificarEjercicio(
            @PathVariable Long ejercicioId,
            @Valid @ModelAttribute ModificarEjercicioRequest request) {

        EjercicioResponse response = modificarEjercicioService.modificarEjercicio(ejercicioId, request);

        return ResponseEntity.ok(response);
    }
}
