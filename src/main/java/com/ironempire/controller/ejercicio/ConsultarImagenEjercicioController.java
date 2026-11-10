package com.ironempire.controller.ejercicio;

import com.ironempire.service.ejercicio.ConsultarImagenEjercicioService;
import com.ironempire.storage.ArchivoDescargado;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ejercicios")
@RequiredArgsConstructor
public class ConsultarImagenEjercicioController {

    private final ConsultarImagenEjercicioService consultarImagenEjercicioService;

    @GetMapping("/{ejercicioId}/imagen")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> consultarImagen(@PathVariable Long ejercicioId) {

        ArchivoDescargado archivo = consultarImagenEjercicioService.obtenerImagen(ejercicioId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivo.tipoContenido()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .header(HttpHeaders.CACHE_CONTROL, "private, no-store")
                .header("X-Content-Type-Options", "nosniff")
                .body(new InputStreamResource(archivo.contenido()));
    }
}