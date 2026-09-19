package com.ironempire.service.plan;

import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.model.Plan;
import com.ironempire.repository.JpaPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarPlanService {

    private final JpaPlanRepository planRepository;

    @Transactional(readOnly = true)
    public List<PlanResponse> consultarPlanesActivos() {

        // Se recuperan los planes que estén activos.
        List<Plan> planesActivos = planRepository.findByActivoTrue();

        /*
         * Recorre la lista de entidades Plan, transforma cada una en un DTO
         * PlanResponse usando el método convertirAResponse, y devuelve una nueva lista
         * con esos resultados.
         */
        return planesActivos.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlanResponse> consultarTodosLosPlanes() {

        List<Plan> planes = planRepository.findAll();

        return planes.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlanResponse consultarPlanPorId(Long id) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un plan con el ID: " + id));

        return convertirAResponse(plan);
    }

    private PlanResponse convertirAResponse(Plan plan) {

        PlanResponse response = new PlanResponse();

        response.setId(plan.getId());
        response.setNombre(plan.getNombre());
        response.setDescripcion(plan.getDescripcion());
        response.setPrecioMensual(plan.getPrecioMensual());
        response.setActivo(plan.getActivo());
        response.setBeneficios(new ArrayList<>(plan.getBeneficios()));

        return response;
    }
}