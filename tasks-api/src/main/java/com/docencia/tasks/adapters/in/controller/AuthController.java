package com.docencia.tasks.adapters.in.controller;

import com.docencia.tasks.adapters.in.api.LoginRequest;
import com.docencia.tasks.adapters.in.api.TokenResponse;
import com.docencia.tasks.infrastructure.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager gestorAutenticacion;
    private final JwtService servicioJwt;

    public AuthController(AuthenticationManager gestorAutenticacion, JwtService servicioJwt) {
        this.gestorAutenticacion = gestorAutenticacion;
        this.servicioJwt = servicioJwt;
    }

    /**
     * Endpoint para iniciar sesión y obtener un token JWT.
     * 
     * @param solicitud Credenciales de acceso.
     * @return Token JWT.
     */
    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest solicitud) {
        try {
            Authentication auth = gestorAutenticacion.authenticate(
                    new UsernamePasswordAuthenticationToken(solicitud.username(), solicitud.password()));

            UserDetails usuario = (UserDetails) auth.getPrincipal();
            String token = servicioJwt.generarToken(usuario);
            return new TokenResponse(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", e);
        }
    }
}
