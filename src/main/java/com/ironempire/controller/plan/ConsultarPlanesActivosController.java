package com.ironempire.controller.plan;

import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.service.plan.ConsultarPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class ConsultarPlanesActivosController {

    private final ConsultarPlanService consultarPlanService;

    @GetMapping("/activos")
    public ResponseEntity<List<PlanResponse>> consultarPlanesActivos() {

        List<PlanResponse> response = consultarPlanService.consultarPlanesActivos();

        return ResponseEntity.ok(response);
    }
}