package com.ironempire.controller.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.service.usuario.ConsultarPerfilPropioService;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class ConsultarPerfilPropioController {

    private final ConsultarPerfilPropioService consultarPerfilPropioService;

    @GetMapping("/perfil")
    // Para que pueda ser ejecutado por cualquiera de los cuatro roles autenticados.
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponse> consultarPerfilPropio(Principal principal) {

        /*
         * "principal.getName()" devuelve el "subject" (sub) del JWT.
         * En nuestro sistema, el subject contiene el email del usuario autenticado.
         */
        String email = principal.getName();

        UsuarioResponse response = consultarPerfilPropioService.consultarPerfilPropio(email);

        return ResponseEntity.ok(response);
    }
}