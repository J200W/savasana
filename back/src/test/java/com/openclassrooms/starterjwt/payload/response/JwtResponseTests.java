package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtResponseTests {

    @Mock
    private JwtResponse jwtResponse;

    @BeforeEach
    public void setUp() {
        jwtResponse = new JwtResponse("token", 1L, "user@gmail.com", "firstname", "lastname", true);
    }

    @Test
    public void testSetAdmin() {
        jwtResponse.setAdmin(false);
        boolean admin = jwtResponse.getAdmin();
        assertFalse(admin);
    }

    @Test
    public void testSetFirstname() {
        jwtResponse.setFirstName("set-firstname");
        String firstname = jwtResponse.getFirstName();
        assertEquals("set-firstname", firstname);
    }

    @Test
    public void testSetLastname() {
        jwtResponse.setLastName("set-lastname");
        String lastname = jwtResponse.getLastName();
        assertEquals("set-lastname", lastname);
    }

    @Test
    public void testSetUserName() {
        jwtResponse.setUsername("set-username@gmail.com");
        String email = jwtResponse.getUsername();
        assertEquals("set-username@gmail.com", email);
    }

    @Test
    public void testSetToken() {
        jwtResponse.setToken("set-token");
        String token = jwtResponse.getToken();
        assertEquals("set-token", token);
    }

    @Test
    public void testSetType() {
        jwtResponse.setType("set-type");
        String type = jwtResponse.getType();
        assertEquals("set-type", type);
    }

    @Test
    public void testSetId() {
        jwtResponse.setId(2L);
        Long id = jwtResponse.getId();
        assertEquals(2L, id);
    }
}
