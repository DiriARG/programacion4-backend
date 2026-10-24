package com.ironempire.specification;

import com.ironempire.dto.request.fichamedica.ConsultarHistorialFichasMedicasRequest;
import com.ironempire.enums.EstadoFichaMedica;
import com.ironempire.model.FichaMedica;
import com.ironempire.model.Usuario;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class FichaMedicaSpecification {

    private FichaMedicaSpecification() {
    }

    // fechaReferencia se utiliza para determinar si el certificado está vigente.
    public static Specification<FichaMedica> conFiltros(
            ConsultarHistorialFichasMedicasRequest filtros,
            LocalDate fechaReferencia) {

        return (root, query, cb) -> {

            List<Predicate> predicados = new ArrayList<>();

            if (filtros.getFechaVencimientoDesde() != null) {
                LocalDate fechaDesde = filtros.getFechaVencimientoDesde();

                predicados.add(
                        cb.greaterThanOrEqualTo(
                                root.<LocalDate>get("fechaVencimiento"),
                                fechaDesde));
            }

            if (filtros.getFechaVencimientoHasta() != null) {
                LocalDate fechaHasta = filtros.getFechaVencimientoHasta();

                predicados.add(
                        cb.lessThanOrEqualTo(
                                root.<LocalDate>get("fechaVencimiento"),
                                fechaHasta));
            }

            if (filtros.getAlumno() != null
                    && !filtros.getAlumno().isBlank()) {

                String texto = escaparCaracteresLike(
                        filtros.getAlumno().trim().toLowerCase(Locale.ROOT));

                String patron = "%" + texto + "%";

                Join<FichaMedica, Usuario> alumno = root.join("alumno", JoinType.INNER);

                predicados.add(cb.or(
                        cb.like(
                                cb.lower(alumno.<String>get("nombre")),
                                patron,
                                '\\'),
                        cb.like(
                                cb.lower(alumno.<String>get("apellido")),
                                patron,
                                '\\'),
                        cb.like(
                                cb.lower(alumno.<String>get("dni")),
                                patron,
                                '\\')));
            }

            /*
             * El estado no se almacena en la entidad: se deduce comparando
             * fechaVencimiento con fechaReferencia.
             */
            if (filtros.getEstado() == EstadoFichaMedica.VIGENTE) {

                predicados.add(
                        cb.greaterThanOrEqualTo(
                                root.<LocalDate>get("fechaVencimiento"),
                                fechaReferencia));

            } else if (filtros.getEstado() == EstadoFichaMedica.VENCIDA) {

                predicados.add(
                        cb.lessThan(
                                root.<LocalDate>get("fechaVencimiento"),
                                fechaReferencia));
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