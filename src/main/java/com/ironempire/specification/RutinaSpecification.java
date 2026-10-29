package com.ironempire.specification;

import com.ironempire.dto.request.rutina.ConsultarRutinasRequest;
import com.ironempire.model.AlumnoTurno;
import com.ironempire.model.Rutina;
import com.ironempire.model.Usuario;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class RutinaSpecification {

    private RutinaSpecification() {
    }

    public static Specification<Rutina> conFiltros(
            ConsultarRutinasRequest filtros,
            Long profesorId) {

        return (root, query, cb) -> {

            List<Predicate> predicados = new ArrayList<>();

            if (filtros.getActiva() != null) {

                predicados.add(
                        cb.equal(
                                root.get("activa"),
                                filtros.getActiva()));
            }

            // Busca por nombre de rutina, nombre del alunmo, apellido, DNI o nombre
            // completo.
            if (filtros.getBusqueda() != null
                    && !filtros.getBusqueda().isBlank()) {

                String texto = escaparCaracteresLike(
                        filtros.getBusqueda().trim().toLowerCase(Locale.ROOT));

                String patron = "%" + texto + "%";

                Join<Rutina, Usuario> alumno = root.join("alumno", JoinType.INNER);

                predicados.add(cb.or(
                        cb.like(
                                cb.lower(root.<String>get("nombre")),
                                patron,
                                '\\'),

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
                                '\\'),
                        cb.like(
                                cb.lower(
                                        cb.concat(
                                                cb.concat(
                                                        cb.lower(alumno.<String>get(
                                                                "nombre")),
                                                        " "),
                                                cb.lower(alumno.<String>get(
                                                        "apellido")))),
                                patron,
                                '\\')));
            }

            /* Primer subconsulta: asociación mediante una rutina.
            Se comprueba si existe alguna rutina del mismo alumno que haya sido creada por el profesor autenticado.
            Además el alumno puede tener otras rutinas creadas por otros profesores. */ 
            Subquery<Long> subconsultaRutina = query.subquery(Long.class);

            // Entidad que se consultará dentro de la subconsulta.
            Root<Rutina> rutinaAsociada = subconsultaRutina.from(Rutina.class);

            subconsultaRutina
                    .select(rutinaAsociada.<Long>get("id"))
                    .where(
                            cb.equal(
                                    rutinaAsociada.get("alumno"),
                                    root.get("alumno")),

                            cb.equal(
                                    rutinaAsociada.get("profesor").get("id"),
                                    profesorId));

            /* Segunda subconsulta: asociación mediante turno.
            Se comprueba si existe alguna inscripción del mismo alumno a un turno dictado por el profesor autenticado. */                        
            Subquery<Long> subconsultaTurno = query.subquery(Long.class);

            Root<AlumnoTurno> inscripcion = subconsultaTurno.from(AlumnoTurno.class);

            subconsultaTurno
                    .select(inscripcion.<Long>get("id"))
                    .where(
                            cb.equal(
                                    inscripcion.get("alumno"),
                                    root.get("alumno")),

                            cb.equal(
                                    inscripcion.get("turno")
                                            .get("profesor")
                                            .get("id"),
                                    profesorId));

            predicados.add(
                    cb.or(
                            cb.exists(subconsultaRutina),
                            cb.exists(subconsultaTurno)));

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