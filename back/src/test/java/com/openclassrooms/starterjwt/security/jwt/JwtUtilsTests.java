package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test de la classe JwtUtils

 */
@SpringBootTest
public class JwtUtilsTests {

    /**
     * Mocker l'objet Logger
     */
    @Mock
    private Logger logger;

    /**
     * Mocker l'objet UserDetailsImpl
     */
    @Mock
    private UserDetailsImpl userPrincipal;

    /**
     * Mocker l'objet Authentication
     */
    @Mock
    private Authentication authentication;

    /**
     * Injection des dépendances pour JwtUtils
     */
    @InjectMocks
    private JwtUtils jwtUtils;

    /**
     * Récupérer les valeurs de jwtSecret et jwtExpirationMs depuis le fichier application.properties
     */
    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    /**
     * Récupérer les valeurs de jwtSecret et jwtExpirationMs depuis le fichier application.properties
     */
    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Définir une valeur pour le nom d'utilisateur
     */
    private final String username = "user@gmail.com";

    /**
     * Mocker les méthodes avant chaque test
     */
    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        // Set values for jwtSecret and jwtExpirationMs
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsername()).thenReturn("user@gmail.com");
        when(authentication.isAuthenticated()).thenReturn(true);

    }

    /**
     * Test de la méthode generateJwtToken
     */
    @Test
    public void testGenerateJwtToken() {
        jwtUtils.generateJwtToken(authentication);
        verify(authentication, times(1)).getPrincipal();
    }

    /**
     * Test de la méthode getUserNameFromJwtToken
     */
    @Test
    public void testGetUserNameFromJwtToken() {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals(username, extractedUsername);
    }

    /**
     * Test de la méthode getUserNameFromJwtToken avec un token invalide
     */
    @Test
    public void testValidateJwtToken() {
        String authToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
        jwtUtils.validateJwtToken(authToken);
        verify(logger, times(0)).error(anyString());
    }

    /**
     * Test de la méthode getUserNameFromJwtToken avec un token invalide
     */
    @Test
    public void testValidateJwtTokenInvalidSignature() {
        String authToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "invalid_secret")
                .compact();

        jwtUtils.validateJwtToken(authToken);
    }

    /**
     * Test de la méthode getUserNameFromJwtToken avec un token invalide
     */
    @Test
    public void testValidateJwtTokenInvalidToken() {
        String authToken =
                "eyJhbGciOiJIUzI1NiJ9..71";
        jwtUtils.validateJwtToken(authToken);
    }

    /**
     * Test de la méthode getUserNameFromJwtToken avec un token invalide
     */
    @Test
    public void testValidateJwtTokenExpired() {
        String authToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() - jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        jwtUtils.validateJwtToken(authToken);
    }
}
