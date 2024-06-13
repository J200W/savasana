package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MessageResponseTests {

    @Mock
    private MessageResponse messageResponse;

    @BeforeEach
    public void setUp() {
        messageResponse = new MessageResponse("message");
    }

    @Test
    public void testSetMessage() {
        messageResponse.setMessage("set-message");
        String message = messageResponse.getMessage();
        assertEquals("set-message", message);
    }
}