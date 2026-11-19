package com.ironempire.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ironempire.model.Pago;

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

    // Listado administrativo paginado. Ningún parámetro es null: el service completa los valores por defecto.
    @EntityGraph(attributePaths = { "alumno", "plan" })
    @Query("""
            select p from Pago p
            where p.fechaPago between :desde and :hasta
              and p.anulado in :estados
              and (lower(concat(p.alumno.nombre, ' ', p.alumno.apellido)) like :texto
                   or lower(concat(p.alumno.apellido, ' ', p.alumno.nombre)) like :texto
                   or p.alumno.dni like :texto)
            """)
    Page<Pago> buscar(@Param("desde") LocalDate desde,
                      @Param("hasta") LocalDate hasta,
                      @Param("estados") Collection<Boolean> estados,
                      @Param("texto") String texto,
                      Pageable pageable);
}