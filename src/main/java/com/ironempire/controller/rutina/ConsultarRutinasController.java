package com.ironempire.controller.rutina;

import com.ironempire.dto.request.rutina.ConsultarRutinasRequest;
import com.ironempire.dto.response.rutina.RutinaListadoPageResponse;
import com.ironempire.service.rutina.ConsultarRutinasService;

import jakarta.validation.Valid;

import java.security.Principal;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
public class ConsultarRutinasController {

    private final ConsultarRutinasService consultarRutinasService;

    @GetMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<RutinaListadoPageResponse> consultarRutinas(
            @Valid @ModelAttribute ConsultarRutinasRequest filtros,Principal principal) {

        String email = principal.getName();

        RutinaListadoPageResponse response = consultarRutinasService.consultarRutinas(filtros, email);

        return ResponseEntity.ok(response);
    }
}