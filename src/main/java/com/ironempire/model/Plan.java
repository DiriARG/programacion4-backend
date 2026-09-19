package com.ironempire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    /*
     * Se utiliza "BigDecimal" para el precio porque es el estándar de Java para
     * manejar plata de forma exacta.
     * Se permite: 8 dígitos enteros y 2 decimales.
     */
    @Column(name = "precio_mensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioMensual;

    @Column(nullable = false)
    private Boolean activo;

    /*
     * @ElementCollection:
     * JPA/Hibernate creará automáticamente una tabla extra llamada "plan_beneficio"
     * (@CollectionTable). Esta tabla tendrá dos columnas:
     * - "plan_id" (que será una clave foránea apuntando al ID de este Plan).
     * - "beneficio" (el texto guardado).
     * No se coloca @OneToMany porque los "beneficios" son simples strings, no son
     * entidades con su propio id.
     */
    @ElementCollection
    @CollectionTable(name = "plan_beneficio", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "beneficio", nullable = false)
    /*
     * Se inicializa la lista ("new ArrayList<>()") para asegurarse de que la lista
     * siempre exista, incluso si está vacia, evitando problemas con nulos.
     */
    private List<String> beneficios = new ArrayList<>();
}