package com.ironempire.controller.turno;

import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.service.turno.GestionarEstadoTurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class DesactivarTurnoController {

    private final GestionarEstadoTurnoService gestionarEstadoTurnoService;

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<TurnoResponse> desactivarTurno(
            @PathVariable Long id) {

        TurnoResponse response = gestionarEstadoTurnoService.desactivarTurno(id);

        return ResponseEntity.ok(response);
    }
}