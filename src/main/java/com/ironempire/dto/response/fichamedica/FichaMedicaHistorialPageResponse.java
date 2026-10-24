package com.ironempire.dto.response.fichamedica;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FichaMedicaHistorialPageResponse {

    // Fichas correspondientes a la página solicitada.
    private List<FichaMedicaHistorialResponse> content;

    private int page;
    private int size;

    // Cantidad total de fichas que cumplen los filtros y páginas disponibles.
    private long totalElements;
    private int totalPages;

    // Metadatos que permiten al frontend controlar la paginación.
    private boolean first;
    private boolean last;
    private boolean empty;
}