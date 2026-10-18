package com.ironempire.dto.response.asistencia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO que encapsula la respuesta paginada del historial de asistencias.
 * Permite enviar al cliente tanto la lista de registros de la página actual
 * como los metadatos de paginación necesarios para renderizar los controles
 * en el frontend (número de página, total de elementos, páginas totales, etc.).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaHistorialPageResponse {

    private List<AsistenciaHistorialResponse> content;

    private int page;
    private int size;

    private long totalElements;
    private int totalPages;

    private boolean first;
    private boolean last;
    private boolean empty;
}