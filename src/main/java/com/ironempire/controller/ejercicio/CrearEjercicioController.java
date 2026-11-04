package com.ironempire.controller.ejercicio;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ironempire.dto.request.ejercicio.CrearEjercicioRequest;
import com.ironempire.dto.response.ejercicio.EjercicioResponse;
import com.ironempire.service.ejercicio.CrearEjercicioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ejercicios")
@RequiredArgsConstructor
public class CrearEjercicioController {

    private final CrearEjercicioService crearEjercicioService;

    // El "consumes" indica que se espera recibir datos de formulario que pueden incluir archivos. 
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<EjercicioResponse> crearEjercicio(
            @Valid @ModelAttribute CrearEjercicioRequest request) {

        EjercicioResponse response = crearEjercicioService.crearEjercicio(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
