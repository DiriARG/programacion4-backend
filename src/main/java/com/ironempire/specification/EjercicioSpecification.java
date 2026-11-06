package com.ironempire.specification;

import com.ironempire.dto.request.ejercicio.ConsultarEjerciciosRequest;
import com.ironempire.model.Ejercicio;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class EjercicioSpecification {

    private EjercicioSpecification() {
    }

    public static Specification<Ejercicio> conFiltros(
            ConsultarEjerciciosRequest filtros) {

        return (root, query, cb) -> {

            List<Predicate> predicados = new ArrayList<>();

            if (filtros.getActivo() != null) {

                predicados.add(
                        cb.equal(
                                root.get("activo"),
                                filtros.getActivo()));
            }

            if (filtros.getBusqueda() != null
                    && !filtros.getBusqueda().isBlank()) {

                String texto = escaparCaracteresLike(
                        filtros.getBusqueda().trim().toLowerCase(Locale.ROOT));

                String patron = "%" + texto + "%";

                predicados.add(cb.or(
                        cb.like(
                                cb.lower(root.<String>get("nombre")),
                                patron,
                                '\\'),

                        cb.like(
                                cb.lower(root.<String>get("grupoMuscular")),
                                patron,
                                '\\')));
            }

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }

    private static String escaparCaracteresLike(String valor) {

        return valor
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
