package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Les tests de TeacherService.
 */
@ExtendWith(MockitoExtension.class)
public class TeacherServiceTests {

    /**
     * Mocker le repository de teacher.
     */
    @Mock
    private TeacherRepository teacherRepository;

    /**
     * Injection des dépendances dans le service de teacher.
     */
    @InjectMocks
    private TeacherService teacherService;

    /**
     * Test de la méthode findAll de TeacherService.
     */
    @Test
    public void testFindAllTeacher() {
        teacherService.findAll();
        verify(teacherRepository, times(1)).findAll();
    }

    /**
     * Test de la méthode findById de TeacherService.
     */
    @Test
    public void testFindTeacherById() {
        Long teacherId = 1L;
        teacherService.findById(teacherId);
        verify(teacherRepository, times(1)).findById(teacherId);
    }
}
