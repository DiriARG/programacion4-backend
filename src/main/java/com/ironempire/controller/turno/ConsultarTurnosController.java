package com.ironempire.controller.turno;

import com.ironempire.dto.response.turno.TurnoListadoResponse;
import com.ironempire.service.turno.ConsultarTurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class ConsultarTurnosController {

    private final ConsultarTurnoService consultarTurnoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<List<TurnoListadoResponse>> consultarTurnos() {

        List<TurnoListadoResponse> response = consultarTurnoService.consultarTurnos();

        return ResponseEntity.ok(response);
    }
}