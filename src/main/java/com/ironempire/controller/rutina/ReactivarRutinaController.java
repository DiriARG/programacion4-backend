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
public class ReactivarRutinaController {

    private final GestionarEstadoRutinaService gestionarEstadoRutinaService;

    @PatchMapping("/{rutinaId}/reactivar")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<RutinaResponse> reactivarRutina(
            @PathVariable Long rutinaId,
            Principal principal) {

        String email = principal.getName();

        RutinaResponse response = gestionarEstadoRutinaService.reactivarRutina(
                rutinaId,
                email);

        return ResponseEntity.ok(response);
    }
}