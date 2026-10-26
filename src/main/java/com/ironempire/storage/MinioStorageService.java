package com.ironempire.storage;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ironempire.exception.ErrorAlmacenamientoException;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public String subirArchivo(
            String nombreObjeto,
            // Representa el flujo de bytes que contiene el archivo que se quiere subir.
            InputStream contenido,
            long tamanio,
            String tipoContenido) {

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(nombreObjeto)
                            .stream(
                                    contenido,
                                    Long.valueOf(tamanio),
                                    /*
                                     * Permite que el SDK determine automáticamente el tamaño de las partes, osea
                                     * los fragmentos en los que se divide un archivo grande cuando se sube a Minio.
                                     */
                                    Long.valueOf(-1L))
                            .contentType(tipoContenido)
                            .build());

            return nombreObjeto;

        } catch (Exception exception) {
            throw new ErrorAlmacenamientoException(
                    "No se pudo almacenar el archivo.",
                    exception);
        }
    }

    public ArchivoDescargado descargarArchivo(String nombreObjeto) {

        try {
            GetObjectResponse respuesta = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(nombreObjeto)
                            .build());

            String tipoContenido = respuesta.headers().get("Content-Type");

            return new ArchivoDescargado(respuesta, tipoContenido);

        } catch (Exception exception) {
            throw new ErrorAlmacenamientoException(
                    "No se pudo recuperar el archivo.",
                    exception);
        }
    }

    public void eliminarArchivo(String nombreObjeto) {

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(nombreObjeto)
                            .build());

        } catch (Exception exception) {
            throw new ErrorAlmacenamientoException(
                    "No se pudo eliminar el archivo.",
                    exception);
        }
    }
}
