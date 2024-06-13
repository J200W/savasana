package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UserMapperTests {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private static UserDto userDto;
    private static UserDto userDto2;

    private static User user;

    @BeforeAll
    static void beforeAll() {
        LocalDateTime localDateTime = LocalDateTime.now();
        userDto = new UserDto(1L, "test@example.com", "John", "Doe", true, "test!1234", localDateTime, localDateTime);
        userDto2 = new UserDto(2L, "test2@example.com", "Jane", "Doe", false, "test2!1234", localDateTime, localDateTime);
    }

    @Test
    public void testToEntity() {
        user = userMapper.toEntity(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.isAdmin(), user.isAdmin());
    }

    @Test
    public void testToDto() {
        userDto = userMapper.toDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.isAdmin(), userDto.isAdmin());
    }

    @Test
    public void testListToEntity() {
        List<UserDto> userDtoList = Arrays.asList(userDto, userDto2);
        List<User> userList = userMapper.toEntity(userDtoList);

        assertNotNull(userList);
        assertEquals(2, userList.size());
    }

    @Test
    public void testListToDto() {
        User user2 = userMapper.toEntity(userDto2);
        List<User> userList = Arrays.asList(user, user2);
        List<UserDto> userDtoList = userMapper.toDto(userList);

        assertNotNull(userDtoList);
        assertEquals(2, userDtoList.size());
    }
}
