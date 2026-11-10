package com.ironempire.controller.rutina;

import com.ironempire.dto.response.rutina.RutinaResponse;
import com.ironempire.service.rutina.ConsultarRutinaService;

import java.security.Principal;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
public class ConsultarRutinaController {

    private final ConsultarRutinaService consultarRutinaService;

    @GetMapping("/{rutinaId}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<RutinaResponse> consultarRutina(@PathVariable Long rutinaId, Principal principal) {

        String email = principal.getName();

        RutinaResponse response = consultarRutinaService.consultarRutina(rutinaId, email);

        return ResponseEntity.ok(response);
    }
}