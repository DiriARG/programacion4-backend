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
@RequestMapping("/api/usuarios/profesores")
@RequiredArgsConstructor
public class CrearProfesorController {

    private final CrearUsuarioService crearUsuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearProfesor(@Valid @RequestBody CrearUsuarioRequest request) {
        UsuarioResponse response = crearUsuarioService.crearProfesor(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}