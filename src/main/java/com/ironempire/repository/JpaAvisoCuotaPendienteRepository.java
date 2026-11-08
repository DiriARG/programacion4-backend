package com.ironempire.repository;

import com.ironempire.model.AvisoCuotaPendiente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface JpaAvisoCuotaPendienteRepository extends JpaRepository<AvisoCuotaPendiente, Long> {

    boolean existsByAlumnoIdAndFechaVencimiento(Long alumnoId, LocalDate fechaVencimiento);
}
