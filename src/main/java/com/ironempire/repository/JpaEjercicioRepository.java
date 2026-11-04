package com.ironempire.repository;

import com.ironempire.model.Ejercicio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEjercicioRepository extends JpaRepository<Ejercicio, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    List<Ejercicio> findByActivoTrueOrderByNombreAscIdAsc();
}
