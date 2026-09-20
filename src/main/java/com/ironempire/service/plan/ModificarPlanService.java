package com.ironempire.service.plan;

import com.ironempire.dto.request.plan.ModificarPlanRequest;
import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.model.Plan;
import com.ironempire.repository.JpaPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ModificarPlanService {

    private final JpaPlanRepository planRepository;

    @Transactional
    public PlanResponse modificarPlan(Long id, ModificarPlanRequest request) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un plan con el ID: " + id));

        /*
         * Si el nombre cambió, se verifica que no exista otro plan con ese mismo
         * nombre.
         * El "equalsIgnoreCase" permite conservar el nombre actual aunque se modifiquen
         * sus mayúsculas o minúsculas (por ejemplo, cambiar "Básico" por "BÁSICO"),
         * evitando una consulta innecesaria a la base de datos.
         */
        if (!plan.getNombre().equalsIgnoreCase(request.getNombre())
                && planRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RecursoExistenteException("Ya existe un plan con ese nombre.");
        }

        plan.setNombre(request.getNombre());
        plan.setDescripcion(request.getDescripcion());
        plan.setPrecioMensual(request.getPrecioMensual());
        /*
         * Reemplaza la lista anterior de beneficios por la nueva lista enviada en el
         * PUT.
         */
        plan.setBeneficios(new ArrayList<>(request.getBeneficios()));

        Plan planModificado = planRepository.save(plan);

        PlanResponse response = new PlanResponse();
        response.setId(planModificado.getId());
        response.setNombre(planModificado.getNombre());
        response.setDescripcion(planModificado.getDescripcion());
        response.setPrecioMensual(planModificado.getPrecioMensual());
        response.setActivo(planModificado.getActivo());
        response.setBeneficios(new ArrayList<>(planModificado.getBeneficios()));

        return response;
    }
}