package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Les tests de UserDetailsServiceImpl.
 */
public class UserDetailsServiceImplTests {

    /**
     * Mock de UserRepository.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Injection des dépendances de UserDetailsServiceImpl.
     */
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * Objet User.
     */
    private User user;

    /**
     * Initialisation des objets avant chaque test.
     */
    @BeforeEach
    public void beforeEach() {
        user = new User(1L, "user@gmail.com", "Lastname", "Firstname", "test!1234", true, LocalDateTime.now(), LocalDateTime.now());

        userRepository = mock(UserRepository.class);
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
        when(userRepository.findByEmail(any(String.class))).thenReturn(java.util.Optional.of(user));
    }

    /**
     * Test de la méthode loadUserByUsername.
     */
    @Test
    public void testLoadUserByUsername() {
        User userRepo = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userRepo.getEmail());

        assertNotNull(userDetails);
    }
}
