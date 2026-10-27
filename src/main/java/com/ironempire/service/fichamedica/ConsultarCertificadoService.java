package com.ironempire.service.fichamedica;

import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.model.FichaMedica;
import com.ironempire.repository.JpaFichaMedicaRepository;
import com.ironempire.storage.ArchivoDescargado;
import com.ironempire.storage.ArchivoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarCertificadoService {

    private final JpaFichaMedicaRepository jpaFichaMedicaRepository;
    private final ArchivoStorageService archivoStorageService;

    @Transactional(readOnly = true)
    public ArchivoDescargado obtenerCertificado(Long fichaMedicaId) {

        FichaMedica fichaMedica = jpaFichaMedicaRepository
                .findById(fichaMedicaId)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("No se encontró una ficha médica con el ID indicado."));

        return archivoStorageService.descargarArchivo(fichaMedica.getCertificadoKeyMinio());
    }
}