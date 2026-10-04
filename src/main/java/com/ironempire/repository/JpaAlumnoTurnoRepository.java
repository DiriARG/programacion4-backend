package com.ironempire.repository;

import com.ironempire.model.AlumnoTurno;

import com.ironempire.enums.DiaSemana;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAlumnoTurnoRepository extends JpaRepository<AlumnoTurno, Long> {

        boolean existsByAlumnoIdAndTurnoId(Long alumnoId, Long turnoId);

        List<AlumnoTurno> findByTurnoId(Long turnoId);

        /*
         * Cuenta cuántos alumnos inscriptos (ALUMNO_TURNO) tienen otro turno activo
         * superpuesto con el nuevo horario.
         */
        @Query("""
                        SELECT COUNT(at)
                        FROM AlumnoTurno at
                        WHERE at.alumno.id IN :alumnoIds
                          AND at.turno.id <> :turnoId
                          AND at.turno.activo = true
                          AND at.turno.diaSemana = :diaSemana
                          AND at.turno.horaInicio < :horaFin
                          AND at.turno.horaFin > :horaInicio
                        """)
        long contarSuperposicionesDeAlumnos(
                        @Param("alumnoIds") List<Long> alumnoIds,
                        @Param("turnoId") Long turnoId,
                        @Param("diaSemana") DiaSemana diaSemana,
                        @Param("horaFin") LocalTime horaFin,
                        @Param("horaInicio") LocalTime horaInicio);
}
