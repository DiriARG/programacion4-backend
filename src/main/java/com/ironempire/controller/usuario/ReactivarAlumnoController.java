package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.GestionarEstadoUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/alumnos")
@RequiredArgsConstructor
public class ReactivarAlumnoController {

    private final GestionarEstadoUsuarioService gestionarEstadoUsuarioService;

    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<UsuarioResponse> reactivarAlumno(
            @PathVariable Long id) {

        UsuarioResponse response = gestionarEstadoUsuarioService.reactivarAlumno(id);

        return ResponseEntity.ok(response);
    }
}