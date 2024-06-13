package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AuthEntryPontJwtTests {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private AuthenticationException authException;

    @Mock
    private Logger logger;

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        authException = new AuthenticationException("Unauthorized error") {};
    }

    @Test
    public void testCommence() throws ServletException, IOException {
        authEntryPointJwt.commence(request, response, authException);
        logger.error("Unauthorized error: {}", authException.getMessage());

        verify(logger, times(1)).error("Unauthorized error: {}", authException.getMessage());
    }
}
