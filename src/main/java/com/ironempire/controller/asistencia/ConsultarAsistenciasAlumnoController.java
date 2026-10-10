package com.ironempire.controller.asistencia;

import com.ironempire.dto.response.asistencia.AsistenciaAlumnoResponse;
import com.ironempire.service.asistencia.ConsultarAsistenciasAlumnoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/alumnos/asistencias")
@RequiredArgsConstructor
public class ConsultarAsistenciasAlumnoController {

    private final ConsultarAsistenciasAlumnoService consultarAsistenciasAlumnoService;

    @GetMapping
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<List<AsistenciaAlumnoResponse>> consultarAsistenciasAlumno(Principal principal) {

        String email = principal.getName();

        List<AsistenciaAlumnoResponse> response = consultarAsistenciasAlumnoService.consultarAsistenciasAlumno(email);

        return ResponseEntity.ok(response);
    }
}