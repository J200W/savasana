package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Les tests de la classe JwtResponse.
 */
@SpringBootTest
public class JwtResponseTests {

    /**
     * Mocker JwtResponse
     */
    @Mock
    private JwtResponse jwtResponse;

    /**
     * Initialisation de JwtResponse
     */
    @BeforeEach
    public void beforeEach() {
        jwtResponse = new JwtResponse("token", 1L, "user@gmail.com", "firstname", "lastname", true);
    }

    /**
     * Test de la méthode getAdmin.
     */
    @Test
    public void testSetAdmin() {
        jwtResponse.setAdmin(false);
        boolean admin = jwtResponse.getAdmin();
        assertFalse(admin);
    }

    /**
     * Test de la méthode setEmail.
     */
    @Test
    public void testSetFirstname() {
        jwtResponse.setFirstName("set-firstname");
        String firstname = jwtResponse.getFirstName();
        assertEquals("set-firstname", firstname);
    }

    /**
     * Test de la méthode setLastname.
     */
    @Test
    public void testSetLastname() {
        jwtResponse.setLastName("set-lastname");
        String lastname = jwtResponse.getLastName();
        assertEquals("set-lastname", lastname);
    }

    /**
     * Test de la méthode setUsername.
     */
    @Test
    public void testSetUserName() {
        jwtResponse.setUsername("set-username@gmail.com");
        String email = jwtResponse.getUsername();
        assertEquals("set-username@gmail.com", email);
    }

    /**
     * Test de la méthode setToken.
     */
    @Test
    public void testSetToken() {
        jwtResponse.setToken("set-token");
        String token = jwtResponse.getToken();
        assertEquals("set-token", token);
    }

    /**
     * Test de la méthode setType
     */
    @Test
    public void testSetType() {
        jwtResponse.setType("set-type");
        String type = jwtResponse.getType();
        assertEquals("set-type", type);
    }

    /**
     * Test de la méthode setId.
     */
    @Test
    public void testSetId() {
        jwtResponse.setId(2L);
        Long id = jwtResponse.getId();
        assertEquals(2L, id);
    }
}
