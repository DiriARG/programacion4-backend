package com.ironempire.service.autenticacion;

import com.ironempire.dto.request.autenticacion.LoginRequest;
import com.ironempire.dto.response.autenticacion.LoginResponse;
import com.ironempire.security.JwtService;
import com.ironempire.security.UsuarioDetails;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginService(
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        /*
         * Envía el email y la contraseña a Spring Security para verificar las
         * credenciales del usuario.
         */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getContrasenia()));
        
        // Obtiene el usuario que fue autenticado mediante getPrincipal().
        UsuarioDetails usuarioDetails = (UsuarioDetails) authentication.getPrincipal();

        String token = jwtService.generarToken(usuarioDetails);

        return new LoginResponse(
                token,
                "Bearer",
                usuarioDetails.getUsuario().getId(),
                usuarioDetails.getUsuario().getNombre(),
                usuarioDetails.getUsuario().getApellido(),
                usuarioDetails.getUsuario().getEmail(),
                usuarioDetails.getUsuario().getRol());
    }
}