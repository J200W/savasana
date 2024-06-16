package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test des méthodes de la classe AuthTokenFilter
 */
@SpringBootTest
public class AuthTokenFilterTests {

    /**
     * Mock de l'objet JwtUtils
     */
    @Mock
    private JwtUtils jwtUtils;

    /**
     * Mock de l'objet UserDetailsServiceImpl
     */
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Mock de l'objet HttpServletRequest pour les tests
     */
    @Mock
    private HttpServletRequest request;

    /**
     * Mock de l'objet HttpServletResponse pour les tests
     */
    @Mock
    private HttpServletResponse response;

    /**
     * Mock de l'objet FilterChain pour les tests
     */
    @Mock
    private FilterChain filterChain;

    /**
     * Injection de dépendances de la classe AuthTokenFilter
     */
    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    /**
     * Initialisation des objets avant chaque test
     */
    @BeforeEach
    public void beforeEach() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Test de la méthode doFilterInternal
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoFilterInternalSuccess() throws ServletException, IOException {
        authTokenFilter.doFilter(request, response, filterChain);
        assertThat(authTokenFilter).isNotNull();
    }

    /**
     * Test de la méthode doFilterInternal
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoFilterInternalFailure() throws ServletException, IOException {
        // Simuler une requête avec un token valide
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");
        when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid_token")).thenReturn("user@gmail.com");

        // Créer un utilisateur
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("password");
        user.setAdmin(false);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>());

        // Simuler le chargement de l'utilisateur
        when(userDetailsService.loadUserByUsername("user@gmail.com")).thenReturn(userDetails);

        // Exécuter la méthode doFilter et vérifier qu'elle appelle la méthode doFilter du FilterChain
        authTokenFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);

        // Verify that authentication is set in the security context
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("user@gmail.com", authentication.getName());
    }

    /**
     * Test de la méthode doFilterInternal
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoFilterInternalJwtNull() throws ServletException, IOException {
        // Simuler une requête avec un token invalide
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid_token");
        when(jwtUtils.validateJwtToken("invalid_token")).thenReturn(false);

        authTokenFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
