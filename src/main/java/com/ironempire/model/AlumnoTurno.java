package com.ironempire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
// Refuerza la regla de negocio que impide inscribir al mismo alumno más de una vez en un mismo turno.
@Table(name = "alumno_turno", uniqueConstraints = {
        @UniqueConstraint(name = "uk_alumno_turno", columnNames = { "alumno_id", "turno_id" })
})
public class AlumnoTurno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Usuario alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;
}