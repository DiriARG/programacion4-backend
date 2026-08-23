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
@Table(name = "ejercicio")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "grupo_muscular", nullable = false)
    private String grupoMuscular;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_key_minio")
    private String imagenKeyMinio;
}
