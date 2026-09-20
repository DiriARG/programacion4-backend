package com.ironempire.service.plan;

import com.ironempire.dto.request.plan.CrearPlanRequest;
import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.mapper.PlanMapper;
import com.ironempire.model.Plan;
import com.ironempire.repository.JpaPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CrearPlanService {

    private final JpaPlanRepository planRepository;
    private final PlanMapper planMapper;

    @Transactional
    public PlanResponse crearPlan(CrearPlanRequest request) {

        if (planRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RecursoExistenteException(
                    "Ya existe un plan con ese nombre.");
        }

        Plan plan = new Plan();
        plan.setNombre(request.getNombre());
        plan.setDescripcion(request.getDescripcion());
        plan.setPrecioMensual(request.getPrecioMensual());
        plan.setActivo(true);
        /*
         * Se crea un "new ArrayList" por dos motivos:
         * 1 - Para que el DTO y el Plan no usen la misma lista en memoria. De esta
         * forma, cada uno tiene su propia lista y se puede modificar la lista del Plan
         * sin afectar la del DTO.
         * 2 - Se asegura que la lista se pueda modificar, algo conveniente para que
         * JPA/Hibernate pueda gestionar los beneficios de la entidad.
         */
        plan.setBeneficios(new ArrayList<>(request.getBeneficios()));

        Plan planGuardado = planRepository.save(plan);

        return planMapper.convertirAResponse(planGuardado);
    }
}