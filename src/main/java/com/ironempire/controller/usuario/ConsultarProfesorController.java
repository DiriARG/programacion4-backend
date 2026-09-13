package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.ConsultarUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/profesores")
@RequiredArgsConstructor
public class ConsultarProfesorController {

    private final ConsultarUsuarioService consultarUsuarioService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<UsuarioResponse> consultarProfesor(
            @PathVariable Long id) {

        UsuarioResponse response = consultarUsuarioService.consultarProfesor(id);

        return ResponseEntity.ok(response);
    }
}
