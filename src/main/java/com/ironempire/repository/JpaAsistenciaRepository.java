package com.ironempire.repository;

import com.ironempire.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public interface JpaAsistenciaRepository extends JpaRepository<Asistencia, Long> {
        List<Asistencia> findByAlumnoIdOrderByFechaDescHoraDesc(Long alumnoId);

        /**
         * Verifica si ya existe una asistencia registrada para el alumno
         * en la fecha indicada dentro del rango de tiempo de ese mismo minuto.
         */
        @Query("""
                        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                        FROM Asistencia a
                        WHERE a.alumno.id = :alumnoId
                          AND a.fecha = :fecha
                          AND a.hora BETWEEN :horaInicioMinuto AND :horaFinMinuto
                        """)
        boolean existeAsistenciaEnMismoMinuto(
                        @Param("alumnoId") Long alumnoId,
                        @Param("fecha") LocalDate fecha,
                        @Param("horaInicioMinuto") LocalTime horaInicioMinuto,
                        @Param("horaFinMinuto") LocalTime horaFinMinuto);
}
