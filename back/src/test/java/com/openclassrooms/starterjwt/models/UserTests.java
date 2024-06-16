package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Les tests de la classe User.
 */
@SpringBootTest
public class UserTests {

    /**
     * Mocker User.
     */
    @Mock
    private User user;

    /**
     * Initialisation de user avant chaque test.
     */
    @BeforeEach
    public void beforeEach() {
        user = new User(1L, "user@gmail.com", "Lastname", "Firstname", "test!1234", true, LocalDateTime.now(), LocalDateTime.now());
    }

    /**
     * Test du constructeur de la classe User - tous les arguments.
     */
    @Test
    public void testAllArgsConstructor() {
        user = new User(1L, "test@example.com", "Doe", "John", "password", true, LocalDateTime.now(), LocalDateTime.now());
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAdmin());
    }

    /**
     * Test du constructeur de la classe User - sans id.
     */
    @Test
    public void testRequiredArgsConstructor() {
        user = new User("test@example.com", "Doe", "John", "password", true);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAdmin());
    }

    /**
     * Test du constructeur de la classe User - sans arguments.
     */
    @Test
    public void testNoArgsConstructor() {
        user = new User();
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getLastName());
        assertNull(user.getFirstName());
        assertNull(user.getPassword());
        assertFalse(user.isAdmin());
    }

    /**
     * Test des exceptions NullPointerException.
     */
    @Test
    public void testNullPointers() {
        assertThrows(NullPointerException.class, () -> {
            user = new User(null, "Doe", "John", "password", true);
        });
        assertThrows(NullPointerException.class, () -> {
            user = new User("user@gmail.com", null, "John", "password", true);
        });
        assertThrows(NullPointerException.class, () -> {
            user = new User("user@gmail.com", "Doe", null, "password", true);
        });
        assertThrows(NullPointerException.class, () -> {
            user = new User("user@gmail.com", "Doe", "John", null, true);
        });
        assertThrows(NullPointerException.class, () -> {
            user = new User(1L, null, "Doe", "John", "password", true, LocalDateTime.now(), LocalDateTime.now());
        });
        assertThrows(NullPointerException.class, () -> {
            user = new User(1L, "user@gmail.com", null, "John", "password", true, LocalDateTime.now(), LocalDateTime.now());
        });
        assertThrows(NullPointerException.class, () -> {
            user = new User(1L, "user@gmail.com", "Doe", null, "password", true, LocalDateTime.now(), LocalDateTime.now());
        });
        assertThrows(NullPointerException.class, () -> {
            user = new User(1L, "user@gmail.com", "Doe", "John", null, true, LocalDateTime.now(), LocalDateTime.now());
        });
    }

    /**
     * Test des setters et getters de la classe User.
     */
    @Test
    public void testSettersAndGetters() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("password");
        user.setAdmin(true);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    /**
     * Test de la méthode equals() et hashCode() de la classe User.
     */
    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User("test@example.com", "Doe", "John", "password", true);
        user1.setId(1L);
        User user2 = new User("test@example.com", "Doe", "John", "password", true);
        user2.setId(1L);
        User user3 = new User("test2@example.com", "Doe", "Jane", "password2", false);
        user3.setId(2L);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    /**
     * Test de la méthode toString() de la classe User.
     */
    @Test
    public void testToString() {
        User user = new User("test@example.com", "Doe", "John", "password", true);
        user.setId(1L);
        String expectedToString = "User(id=1, email=test@example.com, lastName=Doe, firstName=John, password=password, admin=true, createdAt=null, updatedAt=null)";
        assertEquals(expectedToString, user.toString());
        assertEquals(User.builder().toString(),
                "User.UserBuilder(id=null, " +
                        "email=null, lastName=null, firstName=null, " +
                        "password=null, admin=false, createdAt=null, " +
                        "updatedAt=null)");
    }

    /**
     * Test du builder de la classe User.
     */
    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());

        assertThrows(NullPointerException.class, () -> {
            User.builder().email(null).build();
        });
        assertThrows(NullPointerException.class, () -> {
            User.builder().lastName(null).build();
        });
        assertThrows(NullPointerException.class, () -> {
            User.builder().firstName(null).build();
        });
        assertThrows(NullPointerException.class, () -> {
            User.builder().password(null).build();
        });
    }
}
