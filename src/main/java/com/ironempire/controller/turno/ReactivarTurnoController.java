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
public class ReactivarTurnoController {

    private final GestionarEstadoTurnoService gestionarEstadoTurnoService;

    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<TurnoResponse> reactivarTurno(
            @PathVariable Long id) {

        TurnoResponse response = gestionarEstadoTurnoService.reactivarTurno(id);

        return ResponseEntity.ok(response);
    }
}