package com.ironempire.mapper;

import com.ironempire.dto.response.plan.PlanResponse;
import com.ironempire.model.Plan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PlanMapper {

    public PlanResponse convertirAResponse(Plan plan) {

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