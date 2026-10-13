package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.AlumnoDisponibleResponse;
import com.ironempire.service.usuario.ConsultarUsuarioService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios/alumnos/activos")
@RequiredArgsConstructor
public class ConsultarAlumnosActivosController {

    private final ConsultarUsuarioService consultarUsuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<List<AlumnoDisponibleResponse>> consultarAlumnosActivos() {

        List<AlumnoDisponibleResponse> response = consultarUsuarioService.consultarAlumnosActivos();

        return ResponseEntity.ok(response);
    }
}