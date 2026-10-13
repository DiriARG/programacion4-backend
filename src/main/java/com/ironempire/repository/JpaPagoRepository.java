package com.ironempire.repository;

import com.ironempire.model.Pago;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaPagoRepository extends JpaRepository<Pago, Long> {

    // RN-24: un pago vigente (no anulado) por alumno y período.
    boolean existsByAlumnoIdAndFechaVencimientoAndAnuladoFalse(Long alumnoId, LocalDate fechaVencimiento);

    // CU-AL-03: pago vigente del período consultado.
    Optional<Pago> findByAlumnoIdAndFechaVencimientoAndAnuladoFalse(Long alumnoId, LocalDate fechaVencimiento);

    // CU-AL-04: historial completo (incluye anulados, que se informan como tales).
    List<Pago> findByAlumnoIdOrderByFechaVencimientoDesc(Long alumnoId);

    // RN-79: el plan vigente sale del último pago NO anulado.
    Optional<Pago> findTopByAlumnoIdAndAnuladoFalseOrderByFechaVencimientoDesc(Long alumnoId);

    // CU-G-04: información financiera, sin pagos anulados.
    List<Pago> findByFechaVencimientoBetweenAndAnuladoFalse(LocalDate desde, LocalDate hasta);

    // Carga alumno y plan en la misma consulta (recibo y anulación).
    @Query("select p from Pago p join fetch p.alumno join fetch p.plan where p.id = :id")
    Optional<Pago> findConDetalleById(@Param("id") Long id);
}
