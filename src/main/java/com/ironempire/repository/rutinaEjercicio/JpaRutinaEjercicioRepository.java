package com.ironempire.repository.rutinaEjercicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ironempire.model.RutinaEjercicio;

@Repository
public interface JpaRutinaEjercicioRepository
        extends JpaRepository<RutinaEjercicio, Long>, JpaSpecificationExecutor<RutinaEjercicio> {
}
