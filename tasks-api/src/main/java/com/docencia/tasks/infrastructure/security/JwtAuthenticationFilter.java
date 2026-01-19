package com.docencia.tasks.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService servicioJwt;
    private final UserDetailsService servicioDetallesUsuario;

    public JwtAuthenticationFilter(JwtService servicioJwt, UserDetailsService servicioDetallesUsuario) {
        this.servicioJwt = servicioJwt;
        this.servicioDetallesUsuario = servicioDetallesUsuario;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest peticion,
            @NonNull HttpServletResponse respuesta,
            @NonNull FilterChain cadenaFiltros
    ) throws ServletException, IOException {
        final String cabeceraAuth = peticion.getHeader("Authorization");
        final String jwt;
        final String correoUsuario;

        if (cabeceraAuth == null || !cabeceraAuth.startsWith("Bearer ")) {
            cadenaFiltros.doFilter(peticion, respuesta);
            return;
        }

        jwt = cabeceraAuth.substring(7);
        try {
             correoUsuario = servicioJwt.extraerUsuario(jwt);
        } catch (Exception e) {
             cadenaFiltros.doFilter(peticion, respuesta);
             return;
        }

        if (correoUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails detallesUsuario = this.servicioDetallesUsuario.loadUserByUsername(correoUsuario);
            if (servicioJwt.esTokenValido(jwt, detallesUsuario)) {
                UsernamePasswordAuthenticationToken tokenAuth = new UsernamePasswordAuthenticationToken(
                        detallesUsuario,
                        null,
                        detallesUsuario.getAuthorities()
                );
                tokenAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(peticion));
                SecurityContextHolder.getContext().setAuthentication(tokenAuth);
            }
        }
        cadenaFiltros.doFilter(peticion, respuesta);
    }
}
