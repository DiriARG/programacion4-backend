package com.ironempire.repository;

import com.ironempire.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface JpaPagoRepository extends JpaRepository<Pago, Long> {
    // Los conectores lógicos también deben estar estrictamente en inglés (en este
    // caso, el "And").
    boolean existsByAlumnoIdAndFechaVencimiento(Long alumnoId, LocalDate fechaVencimiento);
}
