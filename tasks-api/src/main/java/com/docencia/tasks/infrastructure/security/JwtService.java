package com.docencia.tasks.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secreto;

    @Value("${app.jwt.expiration-minutes}")
    private long expiracionMinutos;

    /**
     * Genera un token JWT para el usuario proporcionado.
     * @param usuario Detalles del usuario.
     * @return Token JWT generado.
     */
    public String generarToken(UserDetails usuario) {
        return generarToken(usuario.getUsername());
    }

    /**
     * Genera un token JWT para el nombre de usuario proporcionado.
     * @param nombreUsuario Nombre del usuario.
     * @return Token JWT generado.
     */
    public String generarToken(String nombreUsuario) {
        return Jwts.builder()
                .subject(nombreUsuario)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiracionMinutos))
                .signWith(obtenerClaveFirma())
                .compact();
    }

    /**
     * Extrae el nombre de usuario del token.
     * @param token Token JWT.
     * @return Nombre de usuario.
     */
    public String extraerUsuario(String token) {
        return extraerReclamo(token, Claims::getSubject);
    }

    /**
     * Valida si el token es válido para el usuario.
     * @param token Token JWT.
     * @param usuario Detalles del usuario.
     * @return Verdadero si es válido, falso en caso contrario.
     */
    public boolean esTokenValido(String token, UserDetails usuario) {
        final String nombreUsuario = extraerUsuario(token);
        return (nombreUsuario.equals(usuario.getUsername()) && !esTokenExpirado(token));
    }

    private boolean esTokenExpirado(String token) {
        return extraerReclamo(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extraerReclamo(String token, Function<Claims, T> resolverReclamos) {
        final Claims reclamos = extraerTodosLosReclamos(token);
        return resolverReclamos.apply(reclamos);
    }

    private Claims extraerTodosLosReclamos(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerClaveFirma() {
        return Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }
}
