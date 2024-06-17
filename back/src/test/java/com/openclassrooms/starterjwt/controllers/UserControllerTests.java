package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Les tests de la classe UserController.
 */
@SpringBootTest
public class UserControllerTests {

    /**
     * MockMvc pour simuler les requêtes HTTP
     */
    private MockMvc mockMvc;

    /**
     * Objet User
     */
    private static User userMock;

    /**
     * Objet UserDto
     */
    private static UserDto userDtoMock;

    /**
     * Mocker UserService
     */
    @MockBean
    private UserService userService;

    /**
     * Mocker UserMapper
     */
    @MockBean
    private UserMapper userMapper;

    /**
     * Mocker UserDetails
     */
    @MockBean
    private UserDetails userDetails;

    /**
     * Mocker Authentication
     */
    @MockBean
    private Authentication authentication;

    /**
     * Initialisation des objets avant les tests
     */
    @MockBean
    private SecurityContext securityContext;

    /**
     * Initialisation des objets avant les tests
     */
    @BeforeAll
    static void beforeAll() {
        LocalDateTime localDateTime = LocalDateTime.now();
        userMock = new User("johndoe@gmail.com", "Doe", "John", "test!1234", false);
        userDtoMock = new UserDto(1L, "johndoe@gmail.com", "Doe", "John", false, "test!1234", localDateTime, localDateTime);
    }

    /**
     * Initialisation du MockMvc avant chaque test
     */
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(
                userService,
                userMapper)
        ).build();

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    /**
     * Test de la méthode findById de UserController.
     * @throws Exception
     */
    @Test
    public void testFindUserById() throws Exception {
        when(userService.findById(1L)).thenReturn(userMock);
        when(userMapper.toDto(userMock)).thenReturn(userDtoMock);
        this.mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk());
    }

    /**
     * Test de la méthode findById de UserController avec un utilisateur non trouvé.
     * @throws Exception
     */
    @Test
    public void testFindUserByIdNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);
        this.mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test de la méthode findById de UserController avec un utilisateur non autorisé.
     * @throws Exception
     */
    @Test
    public void testFindUserByIdFormatIncorrect() throws Exception {
        this.mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la méthode delete de UserController.
     * @throws Exception
     */
    @Test
    public void testDeleteUser() throws Exception {
        when(userService.findById(1L)).thenReturn(userMock);
        when(userDetails.getUsername()).thenReturn("johndoe@gmail.com");
        this.mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
        verify(userService, times(1)).delete(1L);
    }

    /**
     * Test de la méthode delete de UserController avec un utilisateur non trouvé.
     * @throws Exception
     */
    @Test
    public void testDeleteUserNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);
        when(userDetails.getUsername()).thenReturn("johndoe@gmail.com");
        this.mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test de la méthode delete de UserController avec un utilisateur non autorisé.
     * @throws Exception
     */
    @Test
    public void testDeleteUserUnauthorized() throws Exception {
        User anotherUserMock = new User("anotheruser@gmail.com", "Doe", "Jane", "test!1234", false);
        when(userService.findById(1L)).thenReturn(anotherUserMock);
        when(userDetails.getUsername()).thenReturn("johndoe@gmail.com");
        this.mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test de la méthode delete de UserController avec un format incorrect.
     * @throws Exception
     */
    @Test
    public void testDeleteUserFormatIncorrect() throws Exception {
        this.mockMvc.perform(delete("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la méthode findAll de UserController.
     * @throws Exception
     */
    @Test
    public void testNotFoundException() throws Exception {
        this.mockMvc.perform(get("/api/user/100"))
                .andExpect(status().isNotFound());
    }
}