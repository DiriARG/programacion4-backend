package com.ironempire.repository;

import com.ironempire.model.FichaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JpaFichaMedicaRepository
        extends JpaRepository<FichaMedica, Long>, JpaSpecificationExecutor<FichaMedica> {
    Optional<FichaMedica> findByAlumnoId(Long alumnoId);
}
