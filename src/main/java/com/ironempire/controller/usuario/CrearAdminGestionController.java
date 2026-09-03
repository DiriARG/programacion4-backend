package com.ironempire.controller.usuario;

import com.ironempire.dto.request.usuario.CrearUsuarioRequest;
import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.CrearUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios/admin-gestion")
@RequiredArgsConstructor
public class CrearAdminGestionController {

    private final CrearUsuarioService crearUsuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearAdminGestion(@Valid @RequestBody CrearUsuarioRequest request) {
        UsuarioResponse response = crearUsuarioService.crearAdminGestion(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}