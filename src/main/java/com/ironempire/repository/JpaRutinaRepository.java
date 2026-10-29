package com.ironempire.repository;

import com.ironempire.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JpaRutinaRepository extends JpaRepository<Rutina, Long>, JpaSpecificationExecutor<Rutina> {
    Optional<Rutina> findByAlumnoIdAndActivaTrue(Long alumnoId);
}
