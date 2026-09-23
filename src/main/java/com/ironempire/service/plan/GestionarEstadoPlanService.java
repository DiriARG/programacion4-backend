package com.ironempire.service.plan;

import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.PlanMapper;
import com.ironempire.model.Plan;
import com.ironempire.repository.JpaPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GestionarEstadoPlanService {

    private final JpaPlanRepository planRepository;
    private final PlanMapper planMapper;

    @Transactional
    public PlanResponse desactivarPlan(Long id) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un plan con el ID: " + id));

        if (!plan.getActivo()) {
            throw new RecursoInvalidoException(
                    "El plan ya se encuentra inactivo.");
        }

        plan.setActivo(false);

        Plan planDesactivado = planRepository.save(plan);

        return planMapper.convertirAResponse(planDesactivado);
    }

    @Transactional
    public PlanResponse reactivarPlan(Long id) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un plan con el ID: " + id));

        if (plan.getActivo()) {
            throw new RecursoInvalidoException(
                    "El plan ya se encuentra activo.");
        }

        plan.setActivo(true);

        Plan planReactivado = planRepository.save(plan);

        return planMapper.convertirAResponse(planReactivado);
    }
}
