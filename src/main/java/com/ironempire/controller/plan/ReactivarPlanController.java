package com.ironempire.controller.plan;

import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.service.plan.GestionarEstadoPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class ReactivarPlanController {

    private final GestionarEstadoPlanService gestionarEstadoPlanService;

    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<PlanResponse> reactivarPlan(
            @PathVariable Long id) {

        PlanResponse response = gestionarEstadoPlanService.reactivarPlan(id);

        return ResponseEntity.ok(response);
    }
}