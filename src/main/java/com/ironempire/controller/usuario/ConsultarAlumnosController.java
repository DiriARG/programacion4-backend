package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.ConsultarUsuarioService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/alumnos")
@RequiredArgsConstructor
public class ConsultarAlumnosController {

    private final ConsultarUsuarioService consultarUsuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<List<UsuarioResponse>> consultarAlumnos() {

        List<UsuarioResponse> response = consultarUsuarioService.consultarAlumnos();

        return ResponseEntity.ok(response);
    }
}