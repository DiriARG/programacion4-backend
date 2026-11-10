package com.ironempire.controller.rutina;

import com.ironempire.dto.request.rutina.CrearRutinaRequest;
import com.ironempire.dto.response.rutina.RutinaResponse;
import com.ironempire.service.rutina.CrearRutinaService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
public class CrearRutinaController {

    private final CrearRutinaService crearRutinaService;

    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<RutinaResponse> crearRutina(
            @Valid @RequestBody CrearRutinaRequest request, Principal principal) {

        String email = principal.getName();

        RutinaResponse response = crearRutinaService.crearRutina(request, email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }
}