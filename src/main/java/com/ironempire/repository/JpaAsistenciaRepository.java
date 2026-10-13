package com.ironempire.repository;

import com.ironempire.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JpaAsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByAlumnoIdOrderByFechaDescHoraDesc(Long alumnoId);

    // Usado por AvisoCuotaPendienteService: alumnos con al menos una asistencia en el rango.
    @Query("select distinct a.alumno.id from Asistencia a where a.fecha between :desde and :hasta")
    List<Long> findAlumnoIdsConAsistenciaEntre(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}