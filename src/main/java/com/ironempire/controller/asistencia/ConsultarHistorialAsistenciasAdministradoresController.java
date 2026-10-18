package com.ironempire.controller.asistencia;

import com.ironempire.dto.request.asistencia.ConsultarAsistenciasHistorialRequest;
import com.ironempire.dto.response.asistencia.AsistenciaHistorialPageResponse;
import com.ironempire.service.asistencia.ConsultarHistorialAsistenciasService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/asistencias")
@RequiredArgsConstructor
public class ConsultarHistorialAsistenciasAdministradoresController {

    private final ConsultarHistorialAsistenciasService consultarHistorialAsistenciasService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<AsistenciaHistorialPageResponse> consultarHistorial(
            /*
             * Se utiliza "@ModelAttribute" porque los filtros y la paginación viajan como
             * párametros en la URL (query params).
             */
            @Valid @ModelAttribute ConsultarAsistenciasHistorialRequest filtros) {

        AsistenciaHistorialPageResponse response = consultarHistorialAsistenciasService
                .consultarHistorialAdministrativo(filtros);

        return ResponseEntity.ok(response);
    }
}