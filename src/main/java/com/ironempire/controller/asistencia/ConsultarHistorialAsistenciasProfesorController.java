package com.ironempire.controller.asistencia;

import com.ironempire.dto.request.asistencia.ConsultarAsistenciasHistorialRequest;
import com.ironempire.dto.response.asistencia.AsistenciaHistorialPageResponse;
import com.ironempire.service.asistencia.ConsultarHistorialAsistenciasService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profesores/asistencias")
@RequiredArgsConstructor
public class ConsultarHistorialAsistenciasProfesorController {

    private final ConsultarHistorialAsistenciasService consultarHistorialAsistenciasService;

    @GetMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<AsistenciaHistorialPageResponse> consultarHistorial(
            @Valid @ModelAttribute ConsultarAsistenciasHistorialRequest filtros,
            Principal principal) {

        String email = principal.getName();

        AsistenciaHistorialPageResponse response = consultarHistorialAsistenciasService
                .consultarHistorialProfesor(filtros, email);

        return ResponseEntity.ok(response);
    }
}