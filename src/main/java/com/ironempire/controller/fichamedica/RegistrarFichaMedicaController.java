package com.ironempire.controller.fichamedica;

import com.ironempire.dto.request.fichamedica.RegistrarFichaMedicaRequest;
import com.ironempire.dto.response.fichamedica.FichaMedicaResponse;
import com.ironempire.service.fichamedica.GestionarFichaMedicaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fichas-medicas/alumnos")
@RequiredArgsConstructor
public class RegistrarFichaMedicaController {

    private final GestionarFichaMedicaService gestionarFichaMedicaService;

    @PostMapping(value = "/{alumnoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<FichaMedicaResponse> registrarFichaMedica(
            @PathVariable Long alumnoId,
            /*
             * @ModelAttribute porque el cliente envía una petición "multipart/form-data"
             * que contiene tanto un archivo como datos de texto.
             */
            @Valid @ModelAttribute RegistrarFichaMedicaRequest request) {

        FichaMedicaResponse response = gestionarFichaMedicaService.registrarFichaMedica(alumnoId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
