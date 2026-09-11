package com.ironempire.controller.usuario;

import com.ironempire.dto.request.usuario.ModificarUsuarioRequest;
import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.ModificarUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios/admin-gestion")
@RequiredArgsConstructor
public class ModificarAdminGestionController {

    private final ModificarUsuarioService modificarUsuarioService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<UsuarioResponse> modificarAdminGestion(
            @PathVariable Long id,
            @Valid @RequestBody ModificarUsuarioRequest request) {

        UsuarioResponse response = modificarUsuarioService.modificarAdminGestion(id, request);

        return ResponseEntity.ok(response);
    }
}