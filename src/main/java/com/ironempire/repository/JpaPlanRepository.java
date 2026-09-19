package com.ironempire.repository;

import com.ironempire.model.Plan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPlanRepository extends JpaRepository<Plan, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    List<Plan> findByActivoTrue();
}
