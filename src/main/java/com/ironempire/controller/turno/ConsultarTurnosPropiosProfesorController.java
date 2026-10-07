package com.ironempire.controller.turno;

import com.ironempire.dto.response.turno.TurnoPropioProfesorResponse;
import com.ironempire.service.turno.ConsultarTurnosPropiosProfesorService;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
public class ConsultarTurnosPropiosProfesorController {

    private final ConsultarTurnosPropiosProfesorService consultarTurnosPropiosProfesorService;

    @GetMapping("/turnos")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<List<TurnoPropioProfesorResponse>> consultarTurnosPropios(Principal principal) {

        String email = principal.getName();

        List<TurnoPropioProfesorResponse> response = consultarTurnosPropiosProfesorService
                .consultarTurnosPropios(email);

        return ResponseEntity.ok(response);
    }
}