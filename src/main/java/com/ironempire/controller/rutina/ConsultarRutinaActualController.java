package com.ironempire.controller.rutina;

import com.ironempire.dto.response.rutina.RutinaResponse;
import com.ironempire.service.rutina.ConsultarRutinaActualService;

import java.security.Principal;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
public class ConsultarRutinaActualController {

    private final ConsultarRutinaActualService consultarRutinaActualService;

    @GetMapping("/actual")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<RutinaResponse> consultarRutinaActual(
            Principal principal) {

        String email = principal.getName();

        RutinaResponse response = consultarRutinaActualService.consultarRutinaActual(email);

        return ResponseEntity.ok(response);
    }
}
