package com.ironempire.controller.asistencia;

import com.ironempire.dto.request.asistencia.RegistrarAsistenciaRequest;
import com.ironempire.dto.response.asistencia.AsistenciaResponse;
import com.ironempire.service.asistencia.RegistrarAsistenciaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/asistencias")
@RequiredArgsConstructor
public class RegistrarAsistenciaController {

        private final RegistrarAsistenciaService registrarAsistenciaService;

        @PostMapping
        @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL', 'PROFESOR')")
        public ResponseEntity<AsistenciaResponse> registrarAsistencia(
                        @Valid @RequestBody RegistrarAsistenciaRequest request,
                        Principal principal) {

                String email = principal.getName();

                AsistenciaResponse response = registrarAsistenciaService.registrarAsistencia(request, email);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }
}