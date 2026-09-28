package com.ironempire.repository;

import com.ironempire.model.Pago;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaPagoRepository extends JpaRepository<Pago, Long> {
    // Los conectores lógicos también deben estar estrictamente en inglés (en este
    // caso, el "And").
    boolean existsByAlumnoIdAndFechaVencimiento(Long alumnoId, LocalDate fechaVencimiento);

    // CU-AL-03/ RN-24: recupera (si existe) el pago exacto del período consultado.
    Optional<Pago> findByAlumnoIdAndFechaVencimiento(Long alumnoId, LocalDate fechaVencimiento);

    // CU-AL 04 Historial de pagos del alumno, del más reciente primero.
    List<Pago> findByAlumnoIdOrderByFechaVencimientoDesc(Long alumnoId);

    // RN-79: el plan vgente se determina a partir del último pago registrado
    Optional<Pago> findTopByAlumnoOrderByFechaVencimientoDesc(Long alumnoId);

    // CU-G-04: pagos dentro de rango de fechas de vencimento
    List<Pago> findByFechaVencimientoBetween(LocalDate desde, LocalDate hasta);

}
