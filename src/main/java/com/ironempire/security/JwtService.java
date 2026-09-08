package com.ironempire.security;

import com.ironempire.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secreto}")
    private String claveSecreta;

    @Value("${jwt.expiracion}")
    private long expiracion;

    private SecretKey claveFirma;

    /*
     * Convierte la clave secreta configurada en Base64 en una SecretKey
     * que se utilizará para firmar y verificar los JWT.
     *
     * Gracias a @PostConstruct, este proceso se realiza una sola vez
     * al iniciar el servicio, evitando tener que reconstruir la clave
     * cada vez que se genera o valida un token.
     */
    @PostConstruct
    public void inicializar() {
        byte[] bytesClave = Decoders.BASE64.decode(claveSecreta);
        claveFirma = Keys.hmacShaKeyFor(bytesClave);
    }

    // Genera un JWT para el usuario autenticado.
    public String generarToken(UsuarioDetails usuarioDetails) {

        Usuario usuario = usuarioDetails.getUsuario();

        Map<String, Object> datosAdicionales = new HashMap<>();

        datosAdicionales.put("rol", usuario.getRol().name());
        datosAdicionales.put("id", usuario.getId());

        return Jwts.builder()
                // Un claim es simplemente un dato que está contenido dentro del JWT.
                .claims(datosAdicionales)
                .subject(usuarioDetails.getUsername())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + expiracion))
                .signWith(claveFirma)
                // Con toda la data anteriormente proporcionada construye el string del JWT.
                .compact();
    }

    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    /**
     * Verifica que el JWT pertenezca al usuario,
     * no esté expirado y que el usuario siga habilitado.
     */
    public boolean esTokenValido(
            String token,
            UserDetails userDetails) {
        final String email = extraerEmail(token);

        return email.equals(userDetails.getUsername())
                && userDetails.isEnabled()
                && !estaExpirado(token);
    }

    private boolean estaExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration)
                .before(new Date());
    }

    /*
     * Obtiene los claims contenidos en el JWT (datosToken) y utiliza el extractor
     * recibido para obtener el dato específico que se necesita.
     *
     * T representa el tipo de dato que devuelve el extractor, por ejemplo
     * String para el subject o Date para la fecha de expiración.
     */
    private <T> T extraerClaim(
            String token,
            Function<Claims, T> extractor) {
        Claims datosToken = Jwts.parser()
                .verifyWith(claveFirma)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return extractor.apply(datosToken);
    }
}