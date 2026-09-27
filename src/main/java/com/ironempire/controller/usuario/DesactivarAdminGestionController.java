package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.GestionarEstadoUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/admin-gestion")
@RequiredArgsConstructor
public class DesactivarAdminGestionController {

    private final GestionarEstadoUsuarioService gestionarEstadoUsuarioService;

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<UsuarioResponse> desactivarAdminGestion(
            @PathVariable Long id) {

        UsuarioResponse response = gestionarEstadoUsuarioService.desactivarAdminGestion(id);

        return ResponseEntity.ok(response);
    }
}