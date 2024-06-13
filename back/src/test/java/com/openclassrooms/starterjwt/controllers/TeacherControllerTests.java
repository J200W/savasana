package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

@SpringBootTest
public class TeacherControllerTests {

    private MockMvc mockMvc;

    private static Teacher teacherMock;

    private static TeacherDto teacherDtoMock;

    @MockBean
    private TeacherMapper teacherMapper;

    @MockBean
    private TeacherService teacherService;

    @BeforeAll
    static void beforeAll() {
        LocalDateTime localDateTime = LocalDateTime.now();
        teacherMock = new Teacher(1L, "John", "Doe", localDateTime, localDateTime);
        teacherDtoMock = new TeacherDto(1L, "John", "Doe", localDateTime, localDateTime);
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TeacherController(
                teacherService,
                teacherMapper)
        ).build();
    }

    @Test
    public void testFindTeacherById() throws Exception {
        when(teacherService.findById(1L)).thenReturn(teacherMock);
        when(teacherMapper.toDto(teacherMock)).thenReturn(teacherDtoMock);
        this.mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindTeacherByIdNotFound() throws Exception {
        when(teacherService.findById(1L)).thenReturn(null);
        this.mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindTeacherByIdFormatIncorrect() throws Exception {
        this.mockMvc.perform(get("/api/teacher/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindAllTeachers() throws Exception {
        when(teacherService.findAll()).thenReturn(List.of(teacherMock));
        when(teacherMapper.toDto(List.of(teacherMock))).thenReturn(List.of(teacherDtoMock));
        this.mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk());
    }
}
