package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

@SpringBootTest
public class UserControllerTests {

    private MockMvc mockMvc;

    private static User userMock;

    private static UserDto userDtoMock;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserDetails userDetails;

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContext securityContext;

    @BeforeAll
    static void beforeAll() {

        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        LocalDateTime localDateTime = LocalDateTime.now();

        userMock = new User("johndoe@gmail.com", "Doe", "John", "test!1234", false);
        userDtoMock = new UserDto(1L, "johndoe@gmail.com", "Doe", "John", false, "test!1234", localDateTime, localDateTime);
    }

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

    @Test
    public void testFindUserById() throws Exception {
        when(userService.findById(1L)).thenReturn(userMock);
        when(userMapper.toDto(userMock)).thenReturn(userDtoMock);
        this.mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindUserByIdNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);
        this.mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindUserByIdFormatIncorrect() throws Exception {
        this.mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.findById(1L)).thenReturn(userMock);
        when(userDetails.getUsername()).thenReturn("johndoe@gmail.com");
        this.mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);
        when(userDetails.getUsername()).thenReturn("johndoe@gmail.com");
        this.mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUserUnauthorized() throws Exception {
        User anotherUserMock = new User("anotheruser@gmail.com", "Doe", "Jane", "test!1234", false);
        when(userService.findById(1L)).thenReturn(anotherUserMock);
        when(userDetails.getUsername()).thenReturn("johndoe@gmail.com");
        this.mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteUserFormatIncorrect() throws Exception {
        this.mockMvc.perform(delete("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }
}
