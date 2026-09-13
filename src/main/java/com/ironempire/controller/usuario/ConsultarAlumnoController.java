package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.ConsultarUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/alumnos")
@RequiredArgsConstructor
public class ConsultarAlumnoController {

    private final ConsultarUsuarioService consultarUsuarioService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<UsuarioResponse> consultarAlumno(
            @PathVariable Long id) {

        UsuarioResponse response = consultarUsuarioService.consultarAlumno(id);

        return ResponseEntity.ok(response);
    }
}