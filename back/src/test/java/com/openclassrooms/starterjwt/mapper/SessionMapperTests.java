package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Les tests de la classe SessionMapper.
 */
@ExtendWith(MockitoExtension.class)
public class SessionMapperTests {

    /**
     * Mocker SessionMapper
     */
    @Mock
    private TeacherService teacherService;

    /**
     * Mocker UserService
     */
    @Mock
    private UserService userService;

    /**
     * Injection de dépendances de la classe SessionMapper
     */
    @InjectMocks
    private final SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    /**
     * Initialisation sessionDto
     */
    private static SessionDto sessionDto;

    /**
     * Initialisation sessionDto2
     */
    private static SessionDto sessionDto2;

    /**
     * Initialisation teacher
     */
    private static Teacher teacher;

    /**
     * Initialisation de sessionDto et sessionDto2 avant tout les tests.
     */
    @BeforeAll
    static void beforeAll() {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Long> users = List.of(1L);
        sessionDto = new SessionDto(1L, "name", date, 1L, "description", users, localDateTime, localDateTime);
        sessionDto2 = new SessionDto(2L, "name2", date, 2L, "description2", users, localDateTime, localDateTime);
    }

    /**
     * Initialisation de Teacher avant chaque test.
     */
    @BeforeEach
    public void beforeEach() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
    }

    /**
     * Test de la méthode toEntity.
     */
    @Test
    public void testToEntity() {
        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(session);
        assertEquals(sessionDto.getId(), session.getId());
    }

    /**
     * Test de la méthode toEntity.
     */
    @Test
    public void testToDto() {
        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
    }

    /**
     * Test de la méthode toEntity avec un objet null.
     */
    @Test
    public void testToDtoFailure() {
        SessionDto sessionDto = sessionMapper.toDto((Session) null);

        assertNull(sessionDto);
    }

    /**
     * Test de la méthode toEntity avec un objet null.
     */
    @Test
    public void testListToEntity() {
        assertNotNull(sessionMapper.toEntity(List.of(sessionDto, sessionDto2)));
    }

    /**
     * Test de la méthode toEntity avec un objet null.
     */
    @Test
    public void testListToDto() {
        assertNotNull(sessionMapper.toDto(List.of(new Session(), new Session())));
    }
}
