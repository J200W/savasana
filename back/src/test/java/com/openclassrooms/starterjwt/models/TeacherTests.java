package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Les tests de la classe Teacher.
 */
@SpringBootTest
public class TeacherTests {

    /**
     * Mocker Teacher.
     */
    @Mock
    private Teacher teacher;

    /**
     * Initialisation de teacher avant chaque test.
     */
    @BeforeEach
    public void beforeEach() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Firstname teacher");
        teacher.setLastName("Lastname teacher");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Test des getters de la classe Teacher.
     */
    @Test
    public void testPropreties() {
        Teacher teacher2 = new Teacher(2L, "Firstname teacher2", "Lastname teacher2", LocalDateTime.now(), LocalDateTime.now());
        assertNotEquals(teacher, teacher2);
        assertEquals(teacher, teacher);
        teacher.setFirstName("Firstname teacher set");
        assertEquals("Firstname teacher set", teacher.getFirstName());
        teacher.setLastName("Lastname teacher set");
        assertEquals("Lastname teacher set", teacher.getLastName());
        assertEquals(teacher.hashCode(), teacher.hashCode());
        assertEquals(Teacher.builder().toString(), Teacher.builder().toString());
    }
}
