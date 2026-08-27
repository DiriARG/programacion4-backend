package com.ironempire.repository;

import com.ironempire.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaAsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByAlumnoIdOrderByFechaDescHoraDesc(Long alumnoId);
}
