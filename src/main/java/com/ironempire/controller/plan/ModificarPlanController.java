package com.ironempire.controller.plan;

import com.ironempire.dto.request.plan.ModificarPlanRequest;
import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.service.plan.ModificarPlanService;
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
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class ModificarPlanController {

    private final ModificarPlanService modificarPlanService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<PlanResponse> modificarPlan(
            @PathVariable Long id,
            @Valid @RequestBody ModificarPlanRequest request) {

        PlanResponse response = modificarPlanService.modificarPlan(id, request);
        
        return ResponseEntity.ok(response);
    }
}