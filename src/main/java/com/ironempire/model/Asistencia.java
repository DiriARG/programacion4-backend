package com.ironempire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asistencia")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    private Usuario alumno;
    
    // Opcional: una asistencia puede estar asociada a un turno o corresponder a un entrenamiento libre (turno nulo).
    @ManyToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;

    @ManyToOne
    @JoinColumn(name = "registrado_por", nullable = false)
    private Usuario registradoPor;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;
}