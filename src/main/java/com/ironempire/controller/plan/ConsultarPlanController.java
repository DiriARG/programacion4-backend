package com.ironempire.controller.plan;

import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.service.plan.ConsultarPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class ConsultarPlanController {

    private final ConsultarPlanService consultarPlanService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<PlanResponse> consultarPlanPorId(@PathVariable Long id) {

        PlanResponse response = consultarPlanService.consultarPlanPorId(id);

        return ResponseEntity.ok(response);
    }
}