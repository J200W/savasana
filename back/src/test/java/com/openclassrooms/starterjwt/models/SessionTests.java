package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Les tests de la classe Session.
 */
@SpringBootTest
public class SessionTests {

    /**
     * Mocker Session.
     */
    @Mock
    private Session session;

    /**
     * Initialisation de session avant chaque test.
     */
    @BeforeEach
    public void beforeEach() {
        session = new Session();
        session.setId(1L);
        session.setName("session");
        session.setDescription("description");
        session.setUsers(List.of(new User()));
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Test des getters de la classe Session.
     */
    @Test
    public void testPropreties() {
        Session session2 = new Session(2L, "session2", new Date(), "description", new Teacher(), List.of(new User()), LocalDateTime.now(), LocalDateTime.now());
        assertNotEquals(session, session2);
        assertEquals(session, session);
        Date new_date = new Date();
        session.setDate(new_date);
        assertEquals(new_date, session.getDate());
        Teacher teacher = new Teacher();
        session.setTeacher(teacher);
        assertEquals(teacher, session.getTeacher());
        assertEquals(session.hashCode(), session.hashCode());
        assertEquals(Session.builder().toString(), Session.builder().toString());
    }
}
