package com.ironempire.repository;

import com.ironempire.model.AlumnoTurno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAlumnoTurnoRepository extends JpaRepository<AlumnoTurno, Long> {
    boolean existsByAlumnoIdAndTurnoId(Long alumnoId, Long turnoId);
}
