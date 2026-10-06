package com.ironempire.repository;

import com.ironempire.model.AlumnoTurno;

import com.ironempire.enums.DiaSemana;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAlumnoTurnoRepository extends JpaRepository<AlumnoTurno, Long> {

  boolean existsByAlumnoIdAndTurnoId(Long alumnoId, Long turnoId);

  /*
   * Optional permite representar que la inscripción puede no existir y recuperar
   * la entidad para eliminarla.
   */
  Optional<AlumnoTurno> findByAlumnoIdAndTurnoId(Long alumnoId, Long turnoId);

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

  /**
   * Verifica si el alumno ya posee otro turno activo que se superponga
   * con el horario del turno al que se desea inscribir.
   */
  @Query("""
      SELECT CASE WHEN COUNT(at) > 0 THEN true ELSE false END
      FROM AlumnoTurno at
      WHERE at.alumno.id = :alumnoId
        AND at.turno.id <> :turnoId
        AND at.turno.activo = true
        AND at.turno.diaSemana = :diaSemana
        AND at.turno.horaInicio < :horaFin
        AND at.turno.horaFin > :horaInicio
      """)
  boolean existeSuperposicionDeAlumno(
      @Param("alumnoId") Long alumnoId,
      @Param("turnoId") Long turnoId,
      @Param("diaSemana") DiaSemana diaSemana,
      @Param("horaFin") LocalTime horaFin,
      @Param("horaInicio") LocalTime horaInicio);
}
