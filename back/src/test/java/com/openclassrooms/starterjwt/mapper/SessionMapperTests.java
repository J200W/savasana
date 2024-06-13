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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTests {

    private static final Logger log = LoggerFactory.getLogger(SessionMapperTests.class);
    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private final SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    private static SessionDto sessionDto;
    private static SessionDto sessionDto2;

    private static Teacher teacher;

    @BeforeAll
    static void beforeAll() {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Long> users = List.of(1L);
        sessionDto = new SessionDto(1L, "name", date, 1L, "description", users, localDateTime, localDateTime);
        sessionDto2 = new SessionDto(2L, "name2", date, 2L, "description2", users, localDateTime, localDateTime);
    }

    @BeforeEach
    public void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
    }

    @Test
    public void testToEntity() {
        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(session);
        assertEquals(sessionDto.getId(), session.getId());
    }

    @Test
    public void testToDto() {
        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
    }

    @Test
    public void testToDtoFailure() {
        SessionDto sessionDto = sessionMapper.toDto((Session) null);

        assertNull(sessionDto);
    }

    @Test
    public void testListToEntity() {
        assertNotNull(sessionMapper.toEntity(List.of(sessionDto, sessionDto2)));
    }

    @Test
    public void testListToDto() {
        assertNotNull(sessionMapper.toDto(List.of(new Session(), new Session())));
    }
}
