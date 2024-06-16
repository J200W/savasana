package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Les tests de la classe TeacherController.
 */
@SpringBootTest
public class TeacherControllerTests {

    /**
     * Mocker TeacherMapper
     */
    @Mock
    private TeacherMapper teacherMapper;

    /**
     * Mocker TeacherService
     */
    @Mock
    private TeacherService teacherService;

    /**
     * Injection de dépendances de la classe TeacherController
     */
    @InjectMocks
    private TeacherController teacherController;

    /**
     * Initialiser teacherMock
     */
    private static Teacher teacherMock;

    /**
     * Initialiser teacherDtoMock
     */
    private static TeacherDto teacherDtoMock;


    @BeforeAll
    static void beforeAll() {
        LocalDateTime localDateTime = LocalDateTime.now();
        teacherMock = new Teacher(1L, "John", "Doe", localDateTime, localDateTime);
        teacherDtoMock = new TeacherDto(1L, "John", "Doe", localDateTime, localDateTime);
    }

    @BeforeEach
    void beforeEach() {
    }

    /**
     * Test de la méthode findTeacherById.
     * @throws Exception
     */
    @Test
    public void testFindTeacherById() throws Exception {
        // Mocker la méthode findById
        when(teacherService.findById(1L)).thenReturn(teacherMock);
        // Mocker la méthode toDto
        when(teacherMapper.toDto(teacherMock)).thenReturn(teacherDtoMock);

        ResponseEntity<?> result = teacherController.findById("1");
        assertEquals(200, result.getStatusCodeValue());
    }

    /**
     * Test de la méthode findTeacherById avec un id non trouvé.
     * @throws Exception
     */
    @Test
    public void testFindTeacherByIdNotFound() throws Exception {
        // Mocker la méthode findById
        when(teacherService.findById(1L)).thenReturn(null);

        ResponseEntity<?> result = teacherController.findById("1");
        assertEquals(404, result.getStatusCodeValue());
    }

    /**
     * Test de la méthode findTeacherById avec un id incorrect.
     * @throws Exception
     */
    @Test
    public void testFindTeacherByIdFormatIncorrect() throws Exception {
        ResponseEntity<?> result = teacherController.findById("a"); // id incorrect
        assertEquals(400, result.getStatusCodeValue());
    }

    /**
     * Test de la méthode findAllTeachers.
     * @throws Exception
     */
    @Test
    public void testFindAllTeachers() throws Exception {
        // Mocker la méthode findAll
        when(teacherService.findAll()).thenReturn(List.of(teacherMock));
        // Mocker la méthode toDto
        when(teacherMapper.toDto(List.of(teacherMock))).thenReturn(List.of(teacherDtoMock));

        ResponseEntity<?> result = teacherController.findAll();
        assertEquals(200, result.getStatusCodeValue());
    }
}
