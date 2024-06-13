package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TeacherMapperTests {

    private static final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    private static TeacherDto teacherDto;
    private static TeacherDto teacherDto2;

    private static Teacher teacher;

    @BeforeAll
    static void beforeAll() {
        LocalDateTime now = LocalDateTime.now();
        teacherDto = new TeacherDto(1L, "John", "Doe", now, now);
        teacherDto2 = new TeacherDto(2L, "Jane", "Doe", now, now);
    }

    @Test
    public void testToEntity() {
        teacher = teacherMapper.toEntity(teacherDto);

        assertNotNull(teacher);
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
    }

    @Test
    public void testToDto() {
        teacherDto = teacherMapper.toDto(teacher);

        assertNotNull(teacherDto);
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
    }

    @Test
    public void testListToEntity() {
        assertNotNull(teacherMapper.toEntity(Arrays.asList(teacherDto, teacherDto2)));
    }

    @Test
    public void testListToDto() {
        assertNotNull(teacherMapper.toDto(Arrays.asList(teacher, teacher)));
    }
}
