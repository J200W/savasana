package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Les tests de la classe SessionController.
 */
@SpringBootTest
public class SessionControllerTests {

    /**
     * Mocker SessionMapper.
     */
    @Mock
    private SessionMapper sessionMapper;

    /**
     * Mocker SessionService.
     */
    @Mock
    private SessionService sessionService;

    /**
     * Injection de dépendances de la classe SessionController.
     */
    @InjectMocks
    private SessionController sessionController;

    /**
     * Initialiser le mock de Session.
     */
    private static Session sessionMock;

    /**
     * Initialiser le mock de SessionDto.
     */
    private static SessionDto sessionDtoMock;

    /**
     * Initialiser le mock de la liste de Session.
     */
    private static List<Session> sessionsListMock;

    /**
     * Initialiser les mocks.
     */
    @BeforeAll
    static void beforeAll() {
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
     * Tester la méthode findById avec succès.
     * @throws Exception
     */
    @Test
    public void testFindSessionByIdSuccess() throws Exception {
        String id = "1";

        // Mocker le retour de la méthode getById de SessionService
        when(sessionService.getById(Long.valueOf(id))).thenReturn(sessionMock);

        ResponseEntity<?> response = sessionController.findById(id);
        assertEquals(200, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode findById avec session non trouvée.
     * @throws Exception
     */
    @Test
    public void testFindSessionByIdNotFound() throws Exception {
        String id = "99"; // Non trouvé

        // Mocker le retour de la méthode getById de SessionService
        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById(id);
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode findById avec format incorrect.
     * @throws Exception
     */
    @Test
    public void testFindSessionByIdFormatIncorrect() throws Exception {
        String id = "abc"; // Mauvais format

        ResponseEntity<?> response = sessionController.findById(id);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode findAll avec succès.
     * @throws Exception
     */
    @Test
    public void testFindAllSessionSuccess() throws Exception {
        // Mocker le retour de la méthode findAll de SessionService
        when(sessionService.findAll()).thenReturn(sessionsListMock);

        ResponseEntity<?> response = sessionController.findAll();
        assertEquals(200, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode create avec succès.
     * @throws Exception
     */
    @Test
    public void testCreateSessionSuccess() throws Exception {
        // Mocker le retour de la méthode create de SessionService
        when(sessionService.create(sessionMapper.toEntity(sessionDtoMock))).thenReturn(sessionMock);

        ResponseEntity<?> response = sessionController.create(sessionDtoMock);
        assertEquals(200, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode update avec succès.
     * @throws Exception
     */
    @Test
    public void testUpdateSessionSuccess() throws Exception {
        String id = "1";

        // Mocker le retour de la méthode update de SessionService
        when(sessionService.update(Long.parseLong(id), sessionMapper.toEntity(sessionDtoMock))).thenReturn(sessionMock);

        ResponseEntity<?> response = sessionController.update(id, sessionDtoMock);
        assertEquals(200, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode update avec session non trouvée.
     * @throws Exception
     */
    @Test
    public void testUpdateSessionFormatIncorrect() throws Exception {
        String id = "abc"; // Mauvais format

        ResponseEntity<?> response = sessionController.update(id, sessionDtoMock);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode delete avec succès.
     * @throws Exception
     */
    @Test
    public void testDeleteSessionSuccess() throws Exception {
        String id = "1";

        // Mocker le retour de la méthode getById de SessionService
        when(sessionService.getById(Long.valueOf(id))).thenReturn(sessionMock);

        ResponseEntity<?> response = sessionController.save(id);
        assertEquals(200, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode delete avec session non trouvée.
     * @throws Exception
     */
    @Test
    public void testDeleteSessionNotFound() throws Exception {
        String id = "99"; // Non trouvé

        // Mocker le retour de la méthode getById de SessionService
        when(sessionService.getById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = sessionController.save(id);
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode delete avec format incorrect.
     * @throws Exception
     */
    @Test
    public void testDeleteSessionFormatIncorrect() throws Exception {
        String id = "abc"; // Mauvais format

        ResponseEntity<?> response = sessionController.save(id);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode participate avec succès.
     * @throws Exception
     */
    @Test
    public void testParticipateSuccess() throws Exception {
        String id = "1";
        String userId = "1";

        ResponseEntity<?> response = sessionController.participate(id, userId);
        assertEquals(200, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode participate avec format incorrect.
     * @throws Exception
     */
    @Test
    public void testParticipateFormatIncorrect() throws Exception {
        // Mauvais format
        String id = "abc";
        String userId = "abc";

        ResponseEntity<?> response = sessionController.participate(id, userId);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode unParticipate avec succès.
     * @throws Exception
     */
    @Test
    public void testUnParticipateSuccess() throws Exception {
        String id = "1";
        String userId = "1";

        ResponseEntity<?> response = sessionController.noLongerParticipate(id, userId);
        assertEquals(200, response.getStatusCodeValue());
    }

    /**
     * Tester la méthode unParticipate avec format incorrect.
     * @throws Exception
     */
    @Test
    public void testUnParticipateFormatIncorrect() throws Exception {
        // Mauvais format
        String id = "abc";
        String userId = "abc";

        ResponseEntity<?> response = sessionController.noLongerParticipate(id, userId);
        assertEquals(400, response.getStatusCodeValue());
    }
}
