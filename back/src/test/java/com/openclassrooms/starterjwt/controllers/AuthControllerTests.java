package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


/**
 * Les tests de la classe AuthController.
 */
@SpringBootTest
public class AuthControllerTests {

    /**
     * Mocker UserDetailsImpl
     */
    @Mock
    private UserDetailsImpl userDetails;

    /**
     * Mocker UserRepository
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mocker JwtUtils
     */
    @Mock
    private JwtUtils jwtUtils;

    /**
     * Mocker PasswordEncoder
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Mocker AuthenticationManager
     */
    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * Injection de dépendances de la classe AuthController
     */
    @InjectMocks
    private AuthController authController;

    /**
     * Test du login avec succès
     */
    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        // Mocker le processus d'authentification
        userDetails = new UserDetailsImpl(1L, "yoga@studio.com", "test", "Yoga", true, "test!1234");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Mock le processus de génération du token
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("mockedJwtToken");
        
        ResponseEntity<?> result = authController.authenticateUser(loginRequest);
        assertEquals(200, result.getStatusCode().value());
    }

    /**
     * Test du login avec échec
     * @throws Exception
     */
    @Test
    public void testLoginFailure() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("");

        // Mocker le processus d'authentification
        userDetails = new UserDetailsImpl(1L, "", "", "", true, "");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Mocker le processus de génération du token
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("mockedJwtToken");
        
        ResponseEntity<?> result = authController.authenticateUser(loginRequest);
        assertNotNull(result.getStatusCode());
    }

    /**
     * Test de l'inscription avec succès
     * @throws Exception
     */
    @Test
    public void testRegisterSuccess() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga-2@studio.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("Yoga Test");
        signupRequest.setLastName("Studio Test");

        // Mocker le processus d'encodage du mot de passe
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // Mocker les vérifications d'existence de l'utilisateur
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        
        // Mocker la sauvegarde de l'utilisateur
        when(userRepository.save(any())).thenReturn(null);
        
        ResponseEntity<?> result = authController.registerUser(signupRequest);
        assertEquals(200, result.getStatusCode().value());
    }

    /**
     * Test de l'inscription avec échec
     * @throws Exception
     */
    @Test
    public void testRegisterFailure() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("Already");
        signupRequest.setLastName("Exists");

        // Mocker le processus d'encodage du mot de passe
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // Mocker les vérifications d'existence de l'utilisateur
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
        
        ResponseEntity<?> result = authController.registerUser(signupRequest);
        assertEquals(400, result.getStatusCode().value());
    }
}
