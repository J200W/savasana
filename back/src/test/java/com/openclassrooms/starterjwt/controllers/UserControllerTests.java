package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Les tests de la classe UserController.
 */
@SpringBootTest
public class UserControllerTests {

    /**
     * Mocker UserService
     */
    @Mock
    private UserService userService;

    /**
     * Mocker UserMapper
     */
    @Mock
    private UserMapper userMapper;

    /**
     * Injection de dépendances de la classe UserController
     */
    @InjectMocks
    private UserController userController;

    /**
     * Mocker UserDetails
     */
    @Mock
    private UserDetails userDetails;

    /**
     * Mocker Authentication
     */
    @Mock
    private Authentication authentication;

    /**
     * Mocker le contexte de sécurité
     */
    @Mock
    private SecurityContext securityContext;

    /**
     * Initialiser userMock
     */
    private static User userMock;

    /**
     * Initialiser userDtoMock
     */
    private static UserDto userDtoMock;

    /**
     * Initialiser les objets avant les tests
     */
    @BeforeAll
    static void beforeAll() {
        // Initialiser les objets User et UserDto
        LocalDateTime localDateTime = LocalDateTime.now();
        userMock = new User("johndoe@gmail.com", "Doe", "John", "test!1234", false);
        userDtoMock = new UserDto(1L, "johndoe@gmail.com", "Doe", "John", false, "test!1234", localDateTime, localDateTime);
    }

    /**
     * Initialiser le contexte de sécurité Spring avant chaque test
     */
    @BeforeEach
    void beforeEach() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mocker l'objet UserDetails
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    /**
     * Test de la méthode findUserById
     * @throws Exception
     */
    @Test
    public void testFindUserById() throws Exception {
        when(userService.findById(1L)).thenReturn(userMock);
        when(userMapper.toDto(userMock)).thenReturn(userDtoMock);
        ResponseEntity<?> response = userController.findById("1");
        assertEquals(200, response.getStatusCode().value());
    }

    /**
     * Test de la méthode findUserById avec un utilisateur non trouvé
     * @throws Exception
     */
    @Test
    public void testFindUserByIdNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null); // Utilisateur non trouvé

        ResponseEntity<?> response = userController.findById("1");
        assertEquals(404, response.getStatusCode().value());
    }

    /**
     * Test de la méthode findUserById avec un utilisateur non autorisé
     * @throws Exception
     */
    @Test
    public void testFindUserByIdFormatIncorrect() throws Exception {
        ResponseEntity<?> response = userController.findById("abc");
        assertEquals(400, response.getStatusCode().value());
    }

    /**
     * Test de la méthode delete
     * @throws Exception
     */
    @Test
    public void testDeleteUser() throws Exception {
        // Mocker le service UserService et le retour de l'utilisateur
        when(userService.findById(1L)).thenReturn(userMock);
        when(userDetails.getUsername()).thenReturn("johndoe@gmail.com");

        ResponseEntity<?> response = userController.save("1");
        assertEquals(200, response.getStatusCode().value());
        verify(userService, times(1)).delete(1L);
    }

    /**
     * Test de la méthode delete avec un utilisateur non trouvé
     * @throws Exception
     */
    @Test
    public void testDeleteUserNotFound() throws Exception {
        // Mocker le service UserService et le retour de l'utilisateur
        when(userService.findById(1L)).thenReturn(null);
        when(userDetails.getUsername()).thenReturn("johndoe@gmail.com");

        ResponseEntity<?> response = userController.save("1");
        assertEquals(404, response.getStatusCode().value());
    }

    /**
     * Test de la méthode delete avec un utilisateur non autorisé
     * @throws Exception
     */
    @Test
    public void testDeleteUserUnauthorized() throws Exception {
        User anotherUserMock = new User("anotheruser@gmail.com", "Doe", "Jane", "test!1234", false);

        // Mocker le service UserService et le retour de l'utilisateur
        when(userService.findById(1L)).thenReturn(anotherUserMock);
        when(userDetails.getUsername()).thenReturn("johndoe@gmail.com");

        ResponseEntity<?> response = userController.save("1");
        assertEquals(401, response.getStatusCode().value());
    }

    /**
     * Test de la méthode delete avec un format incorrect
     * @throws Exception
     */
    @Test
    public void testDeleteUserFormatIncorrect() throws Exception {
        ResponseEntity<?> response = userController.save("abc"); // Format incorrect
        assertEquals(400, response.getStatusCode().value());
    }

    /**
     * Test de la méthode delete avec un utilisateur non trouvé
     * @throws Exception
     */
    @Test
    public void testNotFoundException() throws Exception {
        ResponseEntity<?> response = userController.findById("100"); // Utilisateur non trouvé
        assertEquals(404, response.getStatusCode().value());
    }
}
