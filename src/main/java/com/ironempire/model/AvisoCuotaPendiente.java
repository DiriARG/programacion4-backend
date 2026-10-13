package com.ironempire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Registro de que ya se avisó a un alumno de su cuota pendiente en un período.
 * La unique constraint evita avisos duplicados aunque el proceso corra más de una vez.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aviso_cuota_pendiente", uniqueConstraints = {
        @UniqueConstraint(name = "uk_aviso_alumno_periodo", columnNames = { "alumno_id", "fecha_vencimiento" })
})
public class AvisoCuotaPendiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Usuario alumno;

    // Vencimiento (último día del mes) del período avisado.
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;
}
