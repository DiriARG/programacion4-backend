package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.ProfesorDisponibleResponse;
import com.ironempire.service.usuario.ConsultarUsuarioService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/profesores/activos")
@RequiredArgsConstructor
public class ConsultarProfesoresActivosController {
    private final ConsultarUsuarioService consultarUsuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<List<ProfesorDisponibleResponse>> consultarProfesoresActivos() {

        List<ProfesorDisponibleResponse> response = consultarUsuarioService.consultarProfesoresActivos();

        return ResponseEntity.ok(response);
    }
}
