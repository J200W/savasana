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

@SpringBootTest
public class AuthTokenFilterTests {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternalSuccess() throws ServletException, IOException {
        authTokenFilter.doFilter(request, response, filterChain);
        assertThat(authTokenFilter).isNotNull();
    }

    @Test
    public void testDoFilterInternalFailure() throws ServletException, IOException {
        // Simulate a valid JWT token and valid user details
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");
        when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid_token")).thenReturn("user@gmail.com");

        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("password");
        user.setAdmin(false);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>());

        when(userDetailsService.loadUserByUsername("user@gmail.com")).thenReturn(userDetails);

        // Call the method to be tested
        authTokenFilter.doFilter(request, response, filterChain);

        // Verify that filterChain.doFilter was called
        verify(filterChain, times(1)).doFilter(request, response);

        // Verify that authentication is set in the security context
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("user@gmail.com", authentication.getName());
    }

    @Test
    public void testDoFilterInternalJwtNull() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid_token");
        when(jwtUtils.validateJwtToken("invalid_token")).thenReturn(false);

        authTokenFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
