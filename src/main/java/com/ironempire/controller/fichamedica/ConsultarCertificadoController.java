package com.ironempire.controller.fichamedica;

import com.ironempire.service.fichamedica.ConsultarCertificadoService;
import com.ironempire.storage.ArchivoDescargado;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fichas-medicas")
@RequiredArgsConstructor
public class ConsultarCertificadoController {

    private final ConsultarCertificadoService consultarCertificadoService;

    @GetMapping("/{fichaMedicaId}/certificado")
    @PreAuthorize("hasAnyRole('ADMIN_GESTION', 'ADMIN_GENERAL')")
    public ResponseEntity<Resource> consultarCertificado(@PathVariable Long fichaMedicaId) {

        ArchivoDescargado archivo = consultarCertificadoService.obtenerCertificado(fichaMedicaId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivo.tipoContenido()))
                /*
                 * Le indica al navegador que intente mostrar el archivo en vez de forzar su
                 * descarga.
                 */
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                // Evita almacenar el certificado en caché.
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                /*
                 * Evita que el navegador intente interpretar el contenido como otro tipo de
                 * archivo.
                 */
                .header("X-Content-Type-Options", "nosniff")
                // Devuelve el contenido del archivo mediante un flujo de datos.
                .body(new InputStreamResource(archivo.contenido()));
    }
}