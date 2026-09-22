package com.ironempire.controller.plan;

import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.service.plan.ConsultarPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class ConsultarTodosLosPlanesController {

    private final ConsultarPlanService consultarPlanService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN_GENERAL')")
    public ResponseEntity<List<PlanResponse>> consultarTodosLosPlanes() {

        List<PlanResponse> planes = consultarPlanService.consultarTodosLosPlanes();

        return ResponseEntity.ok(planes);
    }
}