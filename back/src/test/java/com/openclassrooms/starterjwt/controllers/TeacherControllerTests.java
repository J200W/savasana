
package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Les tests de la classe TeacherController.
 */
@SpringBootTest
public class TeacherControllerTests {

    /**
     * MockMvc pour simuler les requêtes HTTP
     */
    private MockMvc mockMvc;

    /**
     * Objet Teacher
     */
    private static Teacher teacherMock;

    /**
     * Objet TeacherDto
     */
    private static TeacherDto teacherDtoMock;

    /**
     * Mocker TeacherMapper
     */
    @MockBean
    private TeacherMapper teacherMapper;

    /**
     * Mocker TeacherService
     */
    @MockBean
    private TeacherService teacherService;

    /**
     * Initialisation des objets avant les tests
     */
    @BeforeAll
    static void beforeAll() {
        LocalDateTime localDateTime = LocalDateTime.now();
        teacherMock = new Teacher(1L, "John", "Doe", localDateTime, localDateTime);
        teacherDtoMock = new TeacherDto(1L, "John", "Doe", localDateTime, localDateTime);
    }

    /**
     * Initialisation du MockMvc avant chaque test
     */
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TeacherController(
                teacherService,
                teacherMapper)
        ).build();
    }

    /**
     * Test de la méthode findById de TeacherController.
     * @throws Exception
     */
    @Test
    public void testFindTeacherById() throws Exception {
        when(teacherService.findById(1L)).thenReturn(teacherMock);
        when(teacherMapper.toDto(teacherMock)).thenReturn(teacherDtoMock);
        this.mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk());
    }

    /**
     * Test de la méthode findById de TeacherController avec un id non trouvé.
     * @throws Exception
     */
    @Test
    public void testFindTeacherByIdNotFound() throws Exception {
        when(teacherService.findById(1L)).thenReturn(null);
        this.mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test de la méthode findById de TeacherController avec un id incorrect.
     * @throws Exception
     */
    @Test
    public void testFindTeacherByIdFormatIncorrect() throws Exception {
        this.mockMvc.perform(get("/api/teacher/abc"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la méthode findAll de TeacherController.
     * @throws Exception
     */
    @Test
    public void testFindAllTeachers() throws Exception {
        when(teacherService.findAll()).thenReturn(List.of(teacherMock));
        when(teacherMapper.toDto(List.of(teacherMock))).thenReturn(List.of(teacherDtoMock));
        this.mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk());
    }
}