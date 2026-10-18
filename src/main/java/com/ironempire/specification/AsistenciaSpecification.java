package com.ironempire.specification;

import com.ironempire.dto.request.asistencia.ConsultarAsistenciasHistorialRequest;
import com.ironempire.enums.ModalidadAsistencia;
import com.ironempire.model.Asistencia;
import com.ironempire.model.Turno;
import com.ironempire.model.Usuario;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class AsistenciaSpecification {

    // Impide instanciar esta clase ya que solo contiene métodos estáticos.
    private AsistenciaSpecification() {
    }

    // Construye los filtros opcionales del historial.
    public static Specification<Asistencia> conFiltros(
            ConsultarAsistenciasHistorialRequest filtros) {

        /*
         * root representa las asistencias consultadas, query permite configurar
         * la consulta y cb construye las condiciones de búsqueda.
         */
        return (root, query, cb) -> {

            // Acumula las condiciones de búsqueda que correspondan a los filtros recibidos.
            List<Predicate> predicados = new ArrayList<>();

            if (filtros.getFechaDesde() != null) {
                LocalDate fechaDesde = filtros.getFechaDesde();

                predicados.add(
                        cb.greaterThanOrEqualTo(
                                root.<LocalDate>get("fecha"),
                                fechaDesde));
            }

            if (filtros.getFechaHasta() != null) {
                LocalDate fechaHasta = filtros.getFechaHasta();

                predicados.add(
                        cb.lessThanOrEqualTo(
                                root.<LocalDate>get("fecha"),
                                fechaHasta));
            }

            if (filtros.getAlumno() != null
                    && !filtros.getAlumno().isBlank()) {

                String texto = escaparCaracteresLike(
                        filtros.getAlumno().trim().toLowerCase(Locale.ROOT));

                // Los "%" permiten buscar el texto en cualquier parte del nombre, apellido o DNI.
                String patron = "%" + texto + "%";

                // Une la asistencia con su alumno para poder buscar por sus datos personales.
                Join<Asistencia, Usuario> alumno = root.join("alumno", JoinType.INNER);

                /*
                 * Busca el texto en nombre, apellido o DNI. Se usa "or" porque basta
                 * con que coincida uno de los tres campos, y "like" permite coincidencias
                 * parciales.
                 */
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

            // Filtra por modalidad, deducida a partir del turno asociado.
            if (filtros.getModalidad() == ModalidadAsistencia.CON_TURNO) {

                predicados.add(root.get("turno").isNotNull());

            } else if (filtros.getModalidad() == ModalidadAsistencia.LIBRE) {

                predicados.add(root.get("turno").isNull());
            }

            // Si se indicó un turno, limita los resultados a ese turno.
            if (filtros.getTurnoId() != null) {

                Join<Asistencia, Turno> turno = root.join("turno", JoinType.INNER);

                predicados.add(
                        cb.equal(turno.get("id"), filtros.getTurnoId()));
            }

            // Combina todos los filtros con "and": deben cumplirse todas las condiciones
            // agregadas.
            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }

    /*
     * Solo permite ver:
     * 1. Asistencias asociadas a turnos del profesor autenticado.
     * 2. Entrenamientos libres registrados por ese profesor.
     *
     * Esta condición se combinará con los filtros solicitados,
     * sin permitir que estos amplíen la visibilidad del profesor.
     */
    public static Specification<Asistencia> visiblesParaProfesor(
            Long profesorId) {

        return (root, query, cb) -> {

            // Se usa "LEFT JOIN" para conservar las asistencias sin turno (entrenamientos libres).
            Join<Asistencia, Turno> turno = root.join("turno", JoinType.LEFT);

            Join<Turno, Usuario> profesorDelTurno = turno.join("profesor", JoinType.LEFT);

            Join<Asistencia, Usuario> registradoPor = root.join("registradoPor", JoinType.INNER);

            // El turno pertenece al profesor autenticado.
            Predicate turnoPropio = cb.equal(profesorDelTurno.get("id"), profesorId);

            // Si es un entrenamiento libre registrado por ese profesor.
            Predicate entrenamientoLibrePropio = cb.and(
                    root.get("turno").isNull(),
                    cb.equal(registradoPor.get("id"), profesorId));

            return cb.or(turnoPropio, entrenamientoLibrePropio);
        };
    }

    /*
     * Evita que los caracteres "\", "%" y "_" introducidos por el usuario
     * se interpreten como comodines de la búsqueda SQL LIKE, evitando
     * comportamientos extraños y errores.
     */
    private static String escaparCaracteresLike(String valor) {
        return valor
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}