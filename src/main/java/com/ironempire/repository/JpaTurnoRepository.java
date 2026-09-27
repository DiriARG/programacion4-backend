package com.ironempire.repository;

import com.ironempire.model.Turno;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByProfesorIdAndActivoTrue(Long profesorId);
}
