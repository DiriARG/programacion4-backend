package com.ironempire.storage;

import com.ironempire.exception.ArchivoDemasiadoGrandeException;
import com.ironempire.exception.ErrorAlmacenamientoException;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.TipoArchivoNoSoportadoException;

import lombok.RequiredArgsConstructor;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArchivoStorageService {

    private final MinioStorageService minioStorageService;

    // Se utiliza solamente para detectar el tipo MIME del contenido.
    private final Tika tika = new Tika();

    @Value("${spring.servlet.multipart.max-file-size:5MB}")
    private DataSize tamanioMaximoArchivo;

    // Los tres tipos de archivo que actualmente se aceptan.
    private static final Map<String, String> EXTENSIONES_POR_TIPO = Map.of(
            "application/pdf", "pdf",
            "image/jpeg", "jpg",
            "image/png", "png");

    /*
     * Parámetros recibidos del servicio de negocio que solicita almacenar el
     * archivo:
     * archivo --> archivo recibido desde la petición.
     * prefijoObjeto -- > categoría y contexto del archivo.
     * tiposPermitidos --> tipos MIME aceptados por la operación.
     */
    public String subirArchivo(
            MultipartFile archivo,
            String prefijoObjeto,
            Set<String> tiposPermitidos) {

        validarArchivo(archivo);

        String tipoContenido = detectarTipoContenido(archivo);

        if (!tiposPermitidos.contains(tipoContenido)) {
            throw new TipoArchivoNoSoportadoException(
                    "El tipo de archivo no está permitido para esta operación.");
        }

        String extension = EXTENSIONES_POR_TIPO.get(tipoContenido);

        String nombreObjeto = generarNombreObjeto(prefijoObjeto, extension);

        /*
         * Se abre un nuevo flujo de lectura para enviar los bytes del archivo al
         * almacenamiento (subirlo).
         */
        try (InputStream contenido = archivo.getInputStream()) {

            minioStorageService.subirArchivo(
                    nombreObjeto,
                    contenido,
                    archivo.getSize(),
                    tipoContenido);

            // Minio Key que se guarda en la bd.
            return nombreObjeto;

        } catch (IOException exception) {
            throw new ErrorAlmacenamientoException(
                    "No se pudo leer el archivo recibido.",
                    exception);
        }
    }

    public ArchivoDescargado descargarArchivo(String nombreObjeto) {
        return minioStorageService.descargarArchivo(nombreObjeto);
    }

    public void eliminarArchivo(String nombreObjeto) {
        minioStorageService.eliminarArchivo(nombreObjeto);
    }

    // Comprueba que el archivo exista, no esté vacío y respete el límite.
    private void validarArchivo(MultipartFile archivo) {

        if (archivo == null || archivo.isEmpty()) {
            throw new RecursoInvalidoException("El archivo no puede estar vacío.");
        }

        if (archivo.getSize() > tamanioMaximoArchivo.toBytes()) {
            throw new ArchivoDemasiadoGrandeException("El archivo supera el tamaño máximo permitido.");
        }
    }

    // Detecta el tipo MIME a partir del contenido del archivo.
    private String detectarTipoContenido(MultipartFile archivo) {

        /*
         * Abre un flujo de lectura de los bytes del archivo para Tika pueda analziar su
         * contenido e identificar el tipo MIME real. El flujo se cierra automáticamente
         * al finalizar el bloque try.
         */
        try (InputStream contenido = archivo.getInputStream()) {
            String tipoContenido = tika.detect(contenido);

            if (!EXTENSIONES_POR_TIPO.containsKey(tipoContenido)) {
                throw new TipoArchivoNoSoportadoException("El archivo debe ser un PDF, JPEG o PNG válido.");
            }

            return tipoContenido;

        } catch (IOException exception) {
            throw new ErrorAlmacenamientoException(
                    "No se pudo leer el archivo para validar su tipo.",
                    exception);
        }
    }

    // Construye una clave única, reutilizable para cada categoría de archivo.
    private String generarNombreObjeto(
            String prefijoObjeto,
            String extension) {

        // Elimina las barras diagonales sobrantes al inicio y al final del prefijo.
        String prefijo = prefijoObjeto.replaceAll("^/+|/+$", "");

        if (prefijo.isBlank()) {
            throw new IllegalArgumentException("El prefijo del objeto no puede estar vacío.");
        }

        return prefijo + "/" + UUID.randomUUID() + "." + extension;
    }
}
