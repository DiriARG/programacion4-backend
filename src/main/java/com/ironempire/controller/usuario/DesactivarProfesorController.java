package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.GestionarEstadoUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/profesores")
@RequiredArgsConstructor
public class DesactivarProfesorController {

    private final GestionarEstadoUsuarioService gestionarEstadoUsuarioService;

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<UsuarioResponse> desactivarProfesor(
            @PathVariable Long id) {

        UsuarioResponse response = gestionarEstadoUsuarioService.desactivarProfesor(id);

        return ResponseEntity.ok(response);
    }
}