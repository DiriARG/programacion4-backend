package com.ironempire.storage;

import java.io.InputStream;

// Transporta el contenido y el tipo MIME recuperado de Minio.
public record ArchivoDescargado(
        InputStream contenido,
        String tipoContenido) {
}