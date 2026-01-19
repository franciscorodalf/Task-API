package com.docencia.tasks.adapters.in.controller;

import com.docencia.tasks.adapters.in.api.LoginRequest;
import com.docencia.tasks.adapters.in.api.TokenResponse;
import com.docencia.tasks.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    private JwtService jwtService;
    private AuthController authController;


    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secreto",
                "Kraj8AxPPe5XdByv9wN4o4cwhW8ExUoxH3kGIG9oY3MobGgN7zbPmmG2aomaZ7RP6EH17Le6RdX6+k0DPxqbfQ==");
        ReflectionTestUtils.setField(jwtService, "expiracionMinutos", 60L);
        authController = new AuthController(authenticationManager, jwtService);
    }

    /**
     * Test para login exitoso
     */
    @Test
    void login_Exitoso() {
        LoginRequest request = new LoginRequest("user", "password");
        Authentication auth = mock(Authentication.class);
        UserDetails userDetails = User.withUsername("user").password("password").roles("USER").build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);

        TokenResponse response = authController.login(request);

        assertNotNull(response);
        assertNotNull(response.token());
        assertTrue(jwtService.esTokenValido(response.token(), userDetails));
    }

    /**
     * Test para login con credenciales inválidas
     */ 
    @Test
    void login_CredencialesInvalidas() {
        LoginRequest request = new LoginRequest("user", "wrong_password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authController.login(request));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }
}
