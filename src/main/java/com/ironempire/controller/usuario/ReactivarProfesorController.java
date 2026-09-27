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
public class ReactivarProfesorController {

    private final GestionarEstadoUsuarioService gestionarEstadoUsuarioService;

    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<UsuarioResponse> reactivarProfesor(
            @PathVariable Long id) {

        UsuarioResponse response = gestionarEstadoUsuarioService.reactivarProfesor(id);

        return ResponseEntity.ok(response);
    }
}