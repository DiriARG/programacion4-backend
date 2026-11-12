package com.ironempire.controller.rutina;

import com.ironempire.dto.response.rutina.RutinaResponse;
import com.ironempire.service.rutina.GestionarEstadoRutinaService;

import java.security.Principal;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
public class DesactivarRutinaController {

    private final GestionarEstadoRutinaService gestionarEstadoRutinaService;

    @PatchMapping("/{rutinaId}/desactivar")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<RutinaResponse> desactivarRutina(@PathVariable Long rutinaId,Principal principal) {

        String email = principal.getName();

        RutinaResponse response = gestionarEstadoRutinaService.desactivarRutina(rutinaId,email);

        return ResponseEntity.ok(response);
    }
}