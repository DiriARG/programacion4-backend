package com.ironempire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
/* Restricción de unicidad que refuerza la regla de negocio de un único pago por alumno y período. 
El período se determina mediante la fecha de vencimiento, que corresponde al día 10 del mes. */
@Table(name = "pago", uniqueConstraints = {
        @UniqueConstraint(name = "uk_pago_alumno_periodo", columnNames = { "alumno_id", "fecha_vencimiento" })
})
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    private Usuario alumno;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "monto_abonado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoAbonado;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "comprobante_key_minio")
    private String comprobanteKeyMinio;
}