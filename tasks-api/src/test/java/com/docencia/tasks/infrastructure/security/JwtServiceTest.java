package com.docencia.tasks.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String SECRET = "Kraj8AxPPe5XdByv9wN4o4cwhW8ExUoxH3kGIG9oY3MobGgN7zbPmmG2aomaZ7RP6EH17Le6RdX6+k0DPxqbfQ==";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secreto", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiracionMinutos", 60L);
    }

    /**
     * Test para generar un token válido
     */
    @Test
    void generarToken_CreaTokenValido() {
        UserDetails user = User.withUsername("testUser").password("pass").roles("USER").build();
        String token = jwtService.generarToken(user);

        assertNotNull(token);
        assertEquals("testUser", jwtService.extraerUsuario(token));
        assertTrue(jwtService.esTokenValido(token, user));
    }

    /**
     * Test para verificar que el token es válido con un usuario incorrecto
     */
    @Test
    void esTokenValido_FallaConUsuarioIncorrecto() {
        UserDetails user = User.withUsername("testUser").password("pass").roles("USER").build();
        UserDetails otherUser = User.withUsername("otherUser").password("pass").roles("USER").build();

        String token = jwtService.generarToken(user);

        assertFalse(jwtService.esTokenValido(token, otherUser));
    }

    /**
     * Test para verificar que el token es válido con un usuario incorrecto
     */ 
    @Test
    void esTokenValido_FallaConTokenExpirado() {
        ReflectionTestUtils.setField(jwtService, "expiracionMinutos", -1L); // Expirado hace un minuto
        UserDetails user = User.withUsername("testUser").password("pass").roles("USER").build();
        String token = jwtService.generarToken(user);
        
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> jwtService.esTokenValido(token, user));
    }
}
