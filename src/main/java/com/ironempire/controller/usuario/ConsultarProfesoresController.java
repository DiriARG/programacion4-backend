package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.ConsultarUsuarioService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/profesores")
@RequiredArgsConstructor
public class ConsultarProfesoresController {

    private final ConsultarUsuarioService consultarUsuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<List<UsuarioResponse>> consultarProfesores() {

        List<UsuarioResponse> response = consultarUsuarioService.consultarProfesores();

        return ResponseEntity.ok(response);
    }
}
