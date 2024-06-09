package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SessionControllerTests {

    private MockMvc mockMvc;

    private static Session sessionMock;

    private static SessionDto sessionDtoMock;

    private static List<Session> sessionsListMock;

    private static ObjectMapper objectMapper;

    @MockBean
    private SessionMapper sessionMapper;

    @MockBean
    private SessionService sessionService;

    @BeforeAll
    static void beforeAll() {

        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        Date date = new Date();

        List<Long> users = new LinkedList<>();

        users.add(1L);

        LocalDateTime localDateTime = LocalDateTime.now();

        sessionDtoMock = new SessionDto(1L, "name", date, 1L, "description", users, localDateTime, localDateTime);


        sessionMock = new Session();
        sessionsListMock = new LinkedList<>();
        sessionsListMock.addFirst(sessionMock);
        sessionsListMock.addLast(sessionMock);

    }

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new SessionController(
                sessionService,
                sessionMapper)
        ).build();
    }

    @Test
    public void testFindSessionByIdSuccess() throws Exception {
        // Prepare the session id
        String id = "1";

        when(sessionService.getById(Long.valueOf(id))).thenReturn(sessionMock);

        // Call the controller method and assert the result
        mockMvc.perform(get("/api/session/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindSessionByIdNotFound() throws Exception {
        // Prepare the session id
        String id = "99";

        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);

        // Call the controller method and assert the result
        mockMvc.perform(get("/api/session/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindSessionByIdFormatIncorrect() throws Exception {
        // Prepare the session id
        String id = "abc";

        // Call the controller method and assert the result
        mockMvc.perform(get("/api/session/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindAllSessionSuccess() throws Exception {
        when(sessionService.findAll()).thenReturn(sessionsListMock);

        // Call the controller method and assert the result
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateSessionSuccess() throws Exception {
        when(sessionService.create(sessionMapper.toEntity(sessionDtoMock))).thenReturn(sessionMock);

        // Call the controller method and assert the result
        mockMvc.perform(post("/api/session")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDtoMock)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSessionSuccess() throws Exception {
        String id = "1";
        when(sessionService.update(Long.parseLong(id), sessionMapper.toEntity(sessionDtoMock))).thenReturn(sessionMock);

        // Call the controller method and assert the result
        mockMvc.perform(put("/api/session/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDtoMock)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSessionFormatIncorrect() throws Exception {
        String id = "abc";

        // Call the controller method and assert the result
        mockMvc.perform(put("/api/session/" + id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDtoMock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteSessionSuccess() throws Exception {
        String id = "1";
        when(sessionService.getById(Long.valueOf(id))).thenReturn(sessionMock);

        // Call the controller method and assert the result
        mockMvc.perform(delete("/api/session/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteSessionNotFound() throws Exception {
        String id = "99";
        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);

        // Call the controller method and assert the result
        mockMvc.perform(delete("/api/session/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteSessionFormatIncorrect() throws Exception {
        String id = "abc";

        // Call the controller method and assert the result
        mockMvc.perform(delete("/api/session/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testParticipateSuccess() throws Exception {
        String id = "1";
        String userId = "1";

        // Call the controller method and assert the result
        mockMvc.perform(post("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    public void testParticipateFormatIncorrect() throws Exception {
        String id = "abc";
        String userId = "abc";

        // Call the controller method and assert the result
        mockMvc.perform(post("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUnParticipateSuccess() throws Exception {
        String id = "1";
        String userId = "1";

        // Call the controller method and assert the result
        mockMvc.perform(delete("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnParticipateFormatIncorrect() throws Exception {
        String id = "abc";
        String userId = "abc";

        // Call the controller method and assert the result
        mockMvc.perform(delete("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isBadRequest());
    }
}
