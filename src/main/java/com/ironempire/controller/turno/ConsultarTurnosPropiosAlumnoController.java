package com.ironempire.controller.turno;

import com.ironempire.dto.response.turno.TurnoPropioAlumnoResponse;
import com.ironempire.service.turno.ConsultarTurnosPropiosAlumnoService;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alumnos")
@RequiredArgsConstructor
public class ConsultarTurnosPropiosAlumnoController {

    private final ConsultarTurnosPropiosAlumnoService consultarTurnosPropiosAlumnoService;

    @GetMapping("/turnos")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<List<TurnoPropioAlumnoResponse>> consultarTurnosPropios(Principal principal) {

        String email = principal.getName();

        List<TurnoPropioAlumnoResponse> response = consultarTurnosPropiosAlumnoService.consultarTurnosPropios(email);

        return ResponseEntity.ok(response);
    }
}