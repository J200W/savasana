package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JwtUtilsTests {

    @Mock
    private Logger logger;

    @Mock
    private UserDetailsImpl userPrincipal;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final String username = "user@gmail.com";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set values for jwtSecret and jwtExpirationMs
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsername()).thenReturn("user@gmail.com");
        when(authentication.isAuthenticated()).thenReturn(true);

    }

    @Test
    public void testGenerateJwtToken() {
        jwtUtils.generateJwtToken(authentication);
        verify(authentication, times(1)).getPrincipal();
    }

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

    @Test
    public void testValidateJwtTokenInvalidToken() {
        String authToken =
                "eyJhbGciOiJIUzI1NiJ9..71";
        jwtUtils.validateJwtToken(authToken);
    }

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
