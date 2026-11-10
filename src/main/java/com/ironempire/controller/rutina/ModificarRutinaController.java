package com.ironempire.controller.rutina;

import com.ironempire.dto.request.rutina.ModificarRutinaRequest;
import com.ironempire.dto.response.rutina.RutinaResponse;
import com.ironempire.service.rutina.ModificarRutinaService;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
public class ModificarRutinaController {

    private final ModificarRutinaService modificarRutinaService;

    @PutMapping("/{rutinaId}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<RutinaResponse> modificarRutina(
            @PathVariable Long rutinaId,
            @Valid @RequestBody ModificarRutinaRequest request,Principal principal) {

        String email = principal.getName();

        RutinaResponse response = modificarRutinaService.modificarRutina(
                rutinaId,
                request,
                email);

        return ResponseEntity.ok(response);
    }
}
