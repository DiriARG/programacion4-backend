package com.ironempire.service.plan;

import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.PlanMapper;
import com.ironempire.model.Plan;
import com.ironempire.repository.JpaPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarPlanService {

    private final JpaPlanRepository planRepository;
    private final PlanMapper planMapper;

    @Transactional(readOnly = true)
    public List<PlanResponse> consultarPlanesActivos() {

        // Se recuperan los planes que estén activos.
        List<Plan> planesActivos = planRepository.findByActivoTrue();

        /*
         * Recorre la lista de entidades Plan, transforma cada una en un DTO
         * PlanResponse usando el mapper convertirAResponse, y devuelve una nueva lista
         * con esos resultados.
         */
        return planesActivos.stream()
                .map(planMapper::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlanResponse> consultarTodosLosPlanes() {

        List<Plan> planes = planRepository.findAll();

        return planes.stream()
                .map(planMapper::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlanResponse consultarPlanPorId(Long id) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un plan con el ID: " + id));

        return planMapper.convertirAResponse(plan);
    }

}