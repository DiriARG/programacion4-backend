package com.ironempire.controller.turno;

import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.service.turno.GestionarInscripcionTurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class InscribirAlumnoController {

    private final GestionarInscripcionTurnoService gestionarInscripcionTurnoService;

    @PostMapping("/{turnoId}/alumnos/{alumnoId}")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<TurnoResponse> inscribirAlumno(
            @PathVariable Long turnoId,
            @PathVariable Long alumnoId) {

        TurnoResponse response = gestionarInscripcionTurnoService.inscribirAlumno(turnoId, alumnoId);

        return ResponseEntity.ok(response);
    }
}