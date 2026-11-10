package com.ironempire.controller.ejercicio;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.ironempire.dto.response.ejercicio.EjercicioActivoResponse;
import com.ironempire.service.ejercicio.ConsultarEjerciciosActivosService;

@RestController
@RequestMapping("/api/ejercicios")
@RequiredArgsConstructor
public class ConsultarEjerciciosActivosController {

    private final ConsultarEjerciciosActivosService consultarEjerciciosActivosService;

    @GetMapping("/activos")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<List<EjercicioActivoResponse>> consultarEjerciciosActivos() {

        List<EjercicioActivoResponse> response = consultarEjerciciosActivosService.consultarEjerciciosActivos();

        return ResponseEntity.ok(response);
    }
}
