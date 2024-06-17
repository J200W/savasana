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

/**
 * Les tests de la classe SessionController.
 */
@SpringBootTest
public class SessionControllerTests {

    /**
     * MockMvc pour simuler les requêtes HTTP
     */
    private MockMvc mockMvc;

    /**
     * Objet Session
     */
    private static Session sessionMock;

    /**
     * Objet SessionDto
     */
    private static SessionDto sessionDtoMock;

    /**
     * Objet List<Session>
     */
    private static List<Session> sessionsListMock;

    /**
     * ObjectMapper pour convertir les objets en JSON
     */
    private static ObjectMapper objectMapper;

    /**
     * Mocker SessionMapper
     */
    @MockBean
    private SessionMapper sessionMapper;

    /**
     * Mocker SessionService
     */
    @MockBean
    private SessionService sessionService;

    /**
     * Initialisation des objets avant les tests
     */
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

    /**
     * Initialisation du MockMvc
     */
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new SessionController(
                sessionService,
                sessionMapper)
        ).build();
    }

    /**
     * Test de la méthode findSessionById avec succès
     *
     * @throws Exception
     */
    @Test
    public void testFindSessionByIdSuccess() throws Exception {
        // Preparer l'id de la session
        String id = "1";

        when(sessionService.getById(Long.valueOf(id))).thenReturn(sessionMock);

        // Appeler la méthode du contrôleur et vérifier le résultat
        mockMvc.perform(get("/api/session/" + id))
                .andExpect(status().isOk());
    }

    /**
     * Test de la méthode findSessionById avec session non trouvée
     *
     * @throws Exception
     */
    @Test
    public void testFindSessionByIdNotFound() throws Exception {
        String id = "99"; // Non trouvé
        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);

        mockMvc.perform(get("/api/session/" + id))
                .andExpect(status().isNotFound());
    }

    /**
     * Test de la méthode findSessionById avec format incorrect
     * @throws Exception
     */
    @Test
    public void testFindSessionByIdFormatIncorrect() throws Exception {
        String id = "abc"; // Format incorrect

        mockMvc.perform(get("/api/session/" + id))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la méthode findAllSession avec succès
     * @throws Exception
     */
    @Test
    public void testFindAllSessionSuccess() throws Exception {
        when(sessionService.findAll()).thenReturn(sessionsListMock);

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk());
    }

    /**
     * Test de la méthode create avec succès
     * @throws Exception
     */
    @Test
    public void testCreateSessionSuccess() throws Exception {
        when(sessionService.create(sessionMapper.toEntity(sessionDtoMock))).thenReturn(sessionMock);

        mockMvc.perform(post("/api/session")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDtoMock)))
                .andExpect(status().isOk());
    }

    /**
     * Test de la méthode update avec format incorrect
     * @throws Exception
     */
    @Test
    public void testUpdateSessionSuccess() throws Exception {
        String id = "1";
        when(sessionService.update(Long.parseLong(id), sessionMapper.toEntity(sessionDtoMock))).thenReturn(sessionMock);

        mockMvc.perform(put("/api/session/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDtoMock)))
                .andExpect(status().isOk());
    }

    /**
     * Test de la méthode update avec format incorrect
     * @throws Exception
     */
    @Test
    public void testUpdateSessionFormatIncorrect() throws Exception {
        String id = "abc"; // Format incorrect

        mockMvc.perform(put("/api/session/" + id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDtoMock)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la méthode save avec succès
     * @throws Exception
     */
    @Test
    public void testDeleteSessionSuccess() throws Exception {
        String id = "1";
        when(sessionService.getById(Long.valueOf(id))).thenReturn(sessionMock);

        mockMvc.perform(delete("/api/session/" + id))
                .andExpect(status().isOk());
    }

    /**
     * Test de la méthode save avec session non trouvée
     * @throws Exception
     */
    @Test
    public void testDeleteSessionNotFound() throws Exception {
        String id = "99"; // Non trouvé
        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);

        mockMvc.perform(delete("/api/session/" + id))
                .andExpect(status().isNotFound());
    }

    /**
     * Test de la méthode save avec format incorrect
     * @throws Exception
     */
    @Test
    public void testDeleteSessionFormatIncorrect() throws Exception {
        String id = "abc";

        mockMvc.perform(delete("/api/session/" + id))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la méthode participate avec succès
     * @throws Exception
     */
    @Test
    public void testParticipateSuccess() throws Exception {
        String id = "1";
        String userId = "1";

        // Call the controller method and assert the result
        mockMvc.perform(post("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isOk());
    }

    /**
     * Test de la méthode participate avec format incorrect
     * @throws Exception
     */
    @Test
    public void testParticipateFormatIncorrect() throws Exception {
        // Format incorrect
        String id = "abc";
        String userId = "abc";

        mockMvc.perform(post("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la méthode noLongerParticipate avec succès
     * @throws Exception
     */
    @Test
    public void testUnParticipateSuccess() throws Exception {
        String id = "1";
        String userId = "1";

        mockMvc.perform(delete("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isOk());
    }

    /**
     * Test de la méthode noLongerParticipate avec format incorrect
     * @throws Exception
     */
    @Test
    public void testUnParticipateFormatIncorrect() throws Exception {
        // Format incorrect
        String id = "abc";
        String userId = "abc";

        mockMvc.perform(delete("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isBadRequest());
    }
}