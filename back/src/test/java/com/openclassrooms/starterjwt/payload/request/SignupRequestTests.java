package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Les tests de la classe SignupRequest.
 */
@SpringBootTest
public class SignupRequestTests {

    /**
     * Mock SignupRequest pour les tests.
     */
    @Mock
    private SignupRequest signupRequest;

    /**
     * Initialisation des valeurs pour les tests.
     */
    @BeforeEach
    public void beforeEach() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("user@gmail.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");
    }

    /**
     * Test des getters de la classe SignupRequest.
     */
    @Test
    public void testSignupRequestGetters() {
        String email = signupRequest.getEmail();
        assertEquals("user@gmail.com", email);
        String firstName = signupRequest.getFirstName();
        assertEquals("John", firstName);
        String lastName = signupRequest.getLastName();
        assertEquals("Doe", lastName);
        String password = signupRequest.getPassword();
        assertEquals("password", password);
    }

    /**
     * Test des setters de la classe SignupRequest.
     */
    @Test
    public void testSignupRequestSetters() {
        signupRequest.setEmail("set-user@gmail.com");
        String email = signupRequest.getEmail();
        assertEquals("set-user@gmail.com", email);
        signupRequest.setFirstName("set-John");
        String firstName = signupRequest.getFirstName();
        assertEquals("set-John", firstName);
        signupRequest.setLastName("set-Doe");
        String lastName = signupRequest.getLastName();
        assertEquals("set-Doe", lastName);
        signupRequest.setPassword("set-password");
        String password = signupRequest.getPassword();
        assertEquals("set-password", password);
    }

    /**
     * Test de la méthode equals() de la classe SignupRequest.
     */
    @Test
    public void testEquals() {
        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("user@gmail.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password");

        assertEquals(signupRequest, signupRequest2);
        assertEquals(signupRequest.hashCode(), signupRequest2.hashCode());

        assertEquals(signupRequest, signupRequest);
    }

    /**
     * Test de la méthode equals() de la classe SignupRequest avec des valeurs différentes.
     */
    @Test
    public void testNotEquals() {
        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("user2@gmail.com");
        signupRequest2.setFirstName("John2");
        signupRequest2.setLastName("Doe2");
        signupRequest2.setPassword("password2");

        assertNotEquals(signupRequest, signupRequest2);
        assertNotEquals(signupRequest, null);
        assertNotEquals(signupRequest.hashCode(), signupRequest2.hashCode());
    }

    /**
     * Test de la méthode equals() de la classe SignupRequest avec un objet de classe différente.
     */
    @Test
    public void testEqualsDifferentClass() {
        Object differentClassObject = new Object();
        assertNotEquals(signupRequest, differentClassObject);
    }

    /**
     * Test de la méthode equals() de la classe SignupRequest avec des champs null.
     */
    @Test
    public void testEqualsWithNullFields() {
        SignupRequest signupRequestWithNull1 = new SignupRequest();
        SignupRequest signupRequestWithNull2 = new SignupRequest();
        assertEquals(signupRequestWithNull1, signupRequestWithNull2);

        signupRequestWithNull1.setEmail("user@gmail.com");
        assertNotEquals(signupRequestWithNull1, signupRequestWithNull2);
        signupRequestWithNull2.setEmail("user@gmail.com");
        assertEquals(signupRequestWithNull1, signupRequestWithNull2);
    }
}
