package com.ironempire.repository;

import com.ironempire.enums.DiaSemana;
import com.ironempire.model.Turno;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByProfesorIdAndActivoTrue(Long profesorId);

    // ¿Existe algún turno activo de este profesor, en este día, que se superponga con este nuevo horario?
    boolean existsByProfesorIdAndDiaSemanaAndActivoTrueAndHoraInicioLessThanAndHoraFinGreaterThan(
            Long profesorId,
            DiaSemana diaSemana,
            LocalTime horaFin,
            LocalTime horaInicio);
}
