package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Les tests de la classe MessageResponse.
 */
@SpringBootTest
public class MessageResponseTests {

    /**
     * Mocker MessageResponse
     */
    @Mock
    private MessageResponse messageResponse;

    /**
     * Initialisation de MessageResponse
     */
    @BeforeEach
    public void beforeEach() {
        messageResponse = new MessageResponse("message");
    }

    /**
     * Test de la méthode setMessage.
     */
    @Test
    public void testSetMessage() {
        messageResponse.setMessage("set-message");
        String message = messageResponse.getMessage();
        assertEquals("set-message", message);
    }
}