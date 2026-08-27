package com.ironempire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ficha_medica")
public class FichaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "alumno_id", nullable = false, unique = true)
    private Usuario alumno;

    @Column(name = "certificado_key_minio", nullable = false)
    private String certificadoKeyMinio;

    // Java no tiene un tipo "Text"; se utiliza "String" y con "columnDefinition" se indica que MySQL debe guardar el campo como TEXT.
    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;
}
