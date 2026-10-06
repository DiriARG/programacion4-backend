package com.ironempire.repository;

import com.ironempire.enums.DiaSemana;
import com.ironempire.model.Turno;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaTurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByProfesorIdAndActivoTrue(Long profesorId);

    /*
     * ¿Existe algún turno activo de este profesor, en este día, que se superponga
     * con este nuevo horario?
     */
    boolean existsByProfesorIdAndDiaSemanaAndActivoTrueAndHoraInicioLessThanAndHoraFinGreaterThan(
            Long profesorId,
            DiaSemana diaSemana,
            LocalTime horaFin,
            LocalTime horaInicio);

    /**
     * Verifica si existe algún turno activo del profesor que colisione con el
     * nuevo rango horario en el mismo día, excluyendo el turno que se está
     * modificando.
     */
    @Query("""
            SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
            FROM Turno t
            WHERE t.profesor.id = :profesorId
              AND t.diaSemana = :diaSemana
              AND t.activo = true
              AND t.horaInicio < :horaFin
              AND t.horaFin > :horaInicio
              AND t.id <> :turnoId
            """)
    boolean existeSuperposicionDeProfesor(
            @Param("profesorId") Long profesorId,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("horaFin") LocalTime horaFin,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("turnoId") Long turnoId);
}
