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
@RequestMapping("/api/usuarios/alumnos")
@RequiredArgsConstructor
public class CrearAlumnoController {

    // Inyección de dependencia segura e inmutable.
    private final CrearUsuarioService crearUsuarioService;

    @PostMapping
    /* <UsuarioResponse> --> Define que el cuerpo (body) de la respuesta HTTP contendrá un objeto del tipo UsuarioResponse.
    @Valid --> Activa todas las validaciones que tiene el dto "CrearUsuarioRequest". 
    @RequestBody --> Convierte el JSON enviado por el cliente en el objeto Java CrearUsuarioRequest, empaquetándolo en el request para pasárselo luego al servicio. */
    public ResponseEntity<UsuarioResponse> crearAlumno(@Valid @RequestBody CrearUsuarioRequest request) {
        // Delega la creación y reglas de negocio al Service.
        UsuarioResponse response = crearUsuarioService.crearAlumno(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(response);
    }
}