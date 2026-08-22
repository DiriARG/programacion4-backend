package com.ironempire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /* Se utiliza "BigDecimal" para el precio porque es el estándar de Java para manejar plata de forma exacta. 
    Se permite: 8 dígitos enteros y 2 decimales. */
    @Column(name = "precio_mensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioMensual;

    @Column(nullable = false)
    private Boolean activo;
}