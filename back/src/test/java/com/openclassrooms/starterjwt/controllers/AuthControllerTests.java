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

import java.util.Optional;

@SpringBootTest
public class AuthControllerTests {

    private MockMvc mockMvc;

    @MockBean
    private UserDetailsImpl userDetails;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(
                authenticationManager,
                passwordEncoder,
                jwtUtils,
                userRepository)
        ).build();
    }

    @Test
    public void testLoginSuccess() throws Exception {
        // Prepare the login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        // Mock the authentication process
        userDetails = new UserDetailsImpl(1L, "yoga@studio.com", "test", "Yoga", true, "test!1234");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("mockedJwtToken");

        // Perform the test
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testLoginFailure() throws Exception {
        // Prepare the login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("");

        // Mock the authentication process
        userDetails = new UserDetailsImpl(1L, "yoga@studio.com", "test", "Yoga", true, "test!1234");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("mockedJwtToken");

        // Perform the test
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        // Prepare the signup request
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga-2@studio.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("Yoga Test");
        signupRequest.setLastName("Studio Test");

        // Mock the password encoding process
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // Mock user existence checks
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);

        // Mock user save operation
        when(userRepository.save(any())).thenReturn(null);

        // Perform the test
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testRegisterFailure() throws Exception {
        // Prepare the signup request
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("");
        signupRequest.setLastName("");

        // Mock the password encoding process
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // Mock user existence checks
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        // Perform the test
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
