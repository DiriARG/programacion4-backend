package com.ironempire.controller.turno;

import com.ironempire.dto.request.turno.CrearTurnoRequest;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.service.turno.CrearTurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class CrearTurnoController {

    private final CrearTurnoService crearTurnoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<TurnoResponse> crearTurno(
            @Valid @RequestBody CrearTurnoRequest request) {

        TurnoResponse response = crearTurnoService.crearTurno(request);

        return ResponseEntity.ok(response);
    }
}