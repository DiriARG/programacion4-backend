package com.ironempire.controller.fichamedica;

import com.ironempire.dto.response.fichamedica.AlumnoSinFichaMedicaResponse;
import com.ironempire.service.fichamedica.ConsultarAlumnosSinFichaMedicaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fichas-medicas")
@RequiredArgsConstructor
public class ConsultarAlumnosSinFichaMedicaController {

    private final ConsultarAlumnosSinFichaMedicaService consultarAlumnosSinFichaMedicaService;

    @GetMapping("/alumnos-sin-ficha")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<List<AlumnoSinFichaMedicaResponse>> consultar() {

        List<AlumnoSinFichaMedicaResponse> response = consultarAlumnosSinFichaMedicaService.consultar();

        return ResponseEntity.ok(response);
    }
}