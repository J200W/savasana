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

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTests {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    public void testFindAllTeacher() {
        teacherService.findAll();
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    public void testFindTeacherById() {
        Long teacherId = 1L;
        teacherService.findById(teacherId);
        verify(teacherRepository, times(1)).findById(teacherId);
    }
}
