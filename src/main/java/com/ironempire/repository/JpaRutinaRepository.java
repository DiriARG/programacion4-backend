package com.ironempire.repository;

import com.ironempire.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaRutinaRepository extends JpaRepository<Rutina, Long> {
    Optional<Rutina> findByAlumnoIdAndActivaTrue(Long alumnoId);
}
