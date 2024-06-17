package com.openclassrooms.starterjwt.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Les tests de la classe AuthController.
 */

@SpringBootTest
public class AuthControllerTests {

    /**
     * MockMvc pour simuler les requêtes HTTP
     */
    private MockMvc mockMvc;

    /**
     * Mocker UserDetailsImpl
     */
    @MockBean
    private UserDetailsImpl userDetails;

    /**
     * Mocker UserRepository
     */
    @MockBean
    private UserRepository userRepository;

    /**
     * Mocker JwtUtils
     */
    @MockBean
    private JwtUtils jwtUtils;

    /**
     * Mocker PasswordEncoder
     */
    @MockBean
    private PasswordEncoder passwordEncoder;

    /**
     * Mocker AuthenticationManager
     */
    @MockBean
    private AuthenticationManager authenticationManager;

    /**
     * Initialisation du MockMvc
     */
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(
                authenticationManager,
                passwordEncoder,
                jwtUtils,
                userRepository)
        ).build();
    }

    /**
     * Test du login avec succès
     */
    @Test
    public void testLoginSuccess() throws Exception {
        // Préparer la requête de connexion
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        // Mocker le processus d'authentification
        userDetails = new UserDetailsImpl(1L, "yoga@studio.com", "test", "Yoga", true, "test!1234");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("mockedJwtToken");

        // Effectuer le test
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * Test du login avec échec
     * @throws Exception
     */
    @Test
    public void testLoginFailure() throws Exception {
        // Preparer la requête de connexion
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("");

        // Mocker le processus d'authentification
        userDetails = new UserDetailsImpl(1L, "yoga@studio.com", "test", "Yoga", true, "test!1234");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("mockedJwtToken");

        // Effectuer le test
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    /**
     * Test de l'inscription avec succès
     * @throws Exception
     */
    @Test
    public void testRegisterSuccess() throws Exception {
        // Preparer la requête d'inscription
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga-2@studio.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("Yoga Test");
        signupRequest.setLastName("Studio Test");

        // Mocker le processus de cryptage du mot de passe
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // Mocker les vérifications d'existence de l'utilisateur
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);

        // Mocker la sauvegarde de l'utilisateur
        when(userRepository.save(any())).thenReturn(null);

        // Effectuer le test
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * Test de l'inscription avec échec
     * @throws Exception
     */
    @Test
    public void testRegisterFailure() throws Exception {
        // Preparer la requête d'inscription
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("Already");
        signupRequest.setLastName("Exists");

        // Mocker le processus de cryptage du mot de passe
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // Mocker les vérifications d'existence de l'utilisateur
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        // Effectuer le test
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> result.getResponse().getContentAsString().contentEquals("Error: Error: Email is already taken!"))
                .andReturn();
    }
}