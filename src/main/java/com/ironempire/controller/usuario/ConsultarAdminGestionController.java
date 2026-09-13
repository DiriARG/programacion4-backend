package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.ConsultarUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/admin-gestion")
@RequiredArgsConstructor
public class ConsultarAdminGestionController {

    private final ConsultarUsuarioService consultarUsuarioService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<UsuarioResponse> consultarAdminGestion(
            @PathVariable Long id) {

        UsuarioResponse response = consultarUsuarioService.consultarAdminGestion(id);

        return ResponseEntity.ok(response);
    }
}