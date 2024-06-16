package com.openclassrooms.starterjwt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test de SpringBootSecurityJwtApplication
 */
@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {

	@InjectMocks
	private SpringBootSecurityJwtApplication springBootSecurityJwtApplication;

	@Test
	public void contextLoads() {
		assertNotNull(springBootSecurityJwtApplication);
	}

}
