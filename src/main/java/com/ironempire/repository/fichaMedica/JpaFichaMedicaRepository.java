package com.ironempire.repository.fichaMedica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ironempire.model.FichaMedica;

@Repository
public interface JpaFichaMedicaRepository
        extends JpaRepository<FichaMedica, Long>, JpaSpecificationExecutor<FichaMedica> {
}
