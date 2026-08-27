package com.ironempire.repository;

import com.ironempire.model.FichaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaFichaMedicaRepository extends JpaRepository<FichaMedica, Long> {
    Optional<FichaMedica> findByAlumnoId(Long alumnoId);
}
