package com.ironempire.dto.response.rutina;

import com.ironempire.enums.DiaSemana;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RutinaEjercicioResponse {

    private Long id;
    private Long ejercicioId;
    private String ejercicioNombre;
    private String grupoMuscular;
    private String descripcion;
    private Integer series;
    private Integer repeticiones;
    private BigDecimal peso;
    private DiaSemana diaSemana;
    private Integer orden;
}