package com.ironempire.controller.turno;

import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.service.turno.ConsultarTurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class ConsultarTurnoController {

    private final ConsultarTurnoService consultarTurnoService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<TurnoResponse> consultarTurno(@PathVariable Long id) {

        TurnoResponse response = consultarTurnoService.consultarTurno(id);

        return ResponseEntity.ok(response);
    }
}