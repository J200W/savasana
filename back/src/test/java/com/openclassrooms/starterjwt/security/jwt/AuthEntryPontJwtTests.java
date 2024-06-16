package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test des méthodes de la classe AuthEntryPointJwt
 */
@SpringBootTest
public class AuthEntryPontJwtTests {

    /**
     * Objet HttpServletRequest pour les tests
     */
    private HttpServletRequest request;

    /**
     * Objet HttpServletResponse pour les tests
     */
    private HttpServletResponse response;

    /**
     * Objet AuthenticationException pour les tests
     */
    private AuthenticationException authException;

    /**
     * Mock de l'objet Logger
     */
    @Mock
    private Logger logger;

    /**
     * Injection de dépendances de la classe AuthEntryPointJwt
     */
    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    /**
     * Initialisation des objets avant chaque test
     */
    @BeforeEach
    public void beforeEach() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        authException = new AuthenticationException("Unauthorized error") {};
    }

    /**
     * Test de la méthode commence
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testCommence() throws ServletException, IOException {
        authEntryPointJwt.commence(request, response, authException);
        logger.error("Unauthorized error: {}", authException.getMessage());

        verify(logger, times(1)).error("Unauthorized error: {}", authException.getMessage());
    }
}
