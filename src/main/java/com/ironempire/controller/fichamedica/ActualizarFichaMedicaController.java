package com.ironempire.controller.fichamedica;

import com.ironempire.dto.request.fichamedica.ActualizarFichaMedicaRequest;
import com.ironempire.dto.response.fichamedica.FichaMedicaResponse;
import com.ironempire.service.fichamedica.GestionarFichaMedicaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fichas-medicas/alumnos")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
public class ActualizarFichaMedicaController {

        private final GestionarFichaMedicaService gestionarFichaMedicaService;

        @PutMapping(value = "/{alumnoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
        public ResponseEntity<FichaMedicaResponse> actualizarFichaMedica(
                        @PathVariable Long alumnoId,
                        @Valid @ModelAttribute ActualizarFichaMedicaRequest request) {

                FichaMedicaResponse response = gestionarFichaMedicaService.actualizarFichaMedica( alumnoId,request);

                return ResponseEntity.ok(response);
        }
}
