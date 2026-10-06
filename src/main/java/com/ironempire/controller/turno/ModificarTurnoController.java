package com.ironempire.controller.turno;

import com.ironempire.dto.request.turno.ModificarTurnoRequest;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.service.turno.ModificarTurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class ModificarTurnoController {

    private final ModificarTurnoService modificarTurnoService;

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<TurnoResponse> modificarTurno(
            @PathVariable Long id,
            @Valid @RequestBody ModificarTurnoRequest request) {

        TurnoResponse response = modificarTurnoService.modificarTurno(id, request);

        return ResponseEntity.ok(response);
    }
}