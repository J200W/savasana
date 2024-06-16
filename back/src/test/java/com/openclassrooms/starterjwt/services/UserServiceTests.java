package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Test de UserService
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    /**
     * Mocker UserRepository
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Injection de UserRepository dans UserService
     */
    @InjectMocks
    private UserService userService;

    /**
     * Date actuelle
     */
    private LocalDateTime now;

    /**
     * Initialisation de la date actuelle avant chaque test
     */
    @BeforeEach
    public void beforeEach() {
        now = LocalDateTime.now();
    }

    /**
     * Test de la méthode saveUser
     */
    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    /**
     * Test de la méthode findUserById
     */
    @Test
    public void testFindUserById() {
        Long userId = 1L;
        User user = new User("yoga@studio.com", "lastName", "firstName", "test!1234", true);
        user.setId(userId);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(userId);

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findById(userId);
    }

    /**
     * Test de la méthode findUserById si l'utilisateur n'est pas trouvé
     */
    @Test
    public void testFindUserByIdNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User foundUser = userService.findById(userId);

        assertNull(foundUser);
        verify(userRepository, times(1)).findById(userId);
    }
}
