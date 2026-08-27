package com.ironempire.repository;

import com.ironempire.model.RutinaEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaRutinaEjercicioRepository extends JpaRepository<RutinaEjercicio, Long> {
    // List permite obtener múltiples elementos en forma de lista, en este caso los
    // ejercicios de una rutina que cumplen la consulta.
    List<RutinaEjercicio> findByRutinaIdOrderByOrdenAsc(Long rutinaId);
}
