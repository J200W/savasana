package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDetailsImplTests {

    private UserDetailsImpl.UserDetailsImplBuilder builder;

    @Mock
    private UserDetailsImpl userDetailsImpl;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "user@gmail.com", "Lastname", "Firstname", "test!1234", true, LocalDateTime.now(), LocalDateTime.now());

        userDetailsImpl = new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin(), user.getPassword());
    }

    @Test
    public void testBuilder() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(java.util.Optional.of(user));

        User userRepo = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        UserDetailsImpl userDetails = UserDetailsImpl
                .builder()
                .id(userRepo.getId())
                .username(userRepo.getEmail())
                .lastName(userRepo.getLastName())
                .firstName(userRepo.getFirstName())
                .password(userRepo.getPassword())
                .build();

        assertNotNull(userDetails);
    }

    @Test
    public void testGetters() {
        assertEquals(1L, userDetailsImpl.getId());
        assertTrue(userDetailsImpl.getAdmin());
        assertEquals("user@gmail.com", userDetailsImpl.getUsername());
        assertEquals("test!1234", userDetailsImpl.getPassword());
        assertEquals("Firstname", userDetailsImpl.getFirstName());
        assertEquals("Lastname", userDetailsImpl.getLastName());
    }

    @Test
    public void testAccountNonExpired() {
        boolean accountNonExpired = userDetailsImpl.isAccountNonExpired();
        assertTrue(accountNonExpired);
    }

    @Test
    public void testAccountNonLocked() {
        boolean accountNonLocked = userDetailsImpl.isAccountNonLocked();
        assertTrue(accountNonLocked);
    }

    @Test
    public void testCredentialsNonExpired() {
        boolean credentialsNonExpired = userDetailsImpl.isCredentialsNonExpired();
        assertTrue(credentialsNonExpired);
    }

    @Test
    public void testEnabled() {
        boolean enabled = userDetailsImpl.isEnabled();
        assertTrue(enabled);
    }

    @Test
    public void testEquals() {
        User user2 = new User(2L, "user2@gmail.com", "Lastname 2", "Firstname 2", "test!123456", false, LocalDateTime.now(), LocalDateTime.now());
        assertEquals(userDetailsImpl, new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin(), user.getPassword()));
        assertNotEquals(userDetailsImpl, new UserDetailsImpl(user2.getId(), user2.getEmail(), user2.getFirstName(), user2.getLastName(), user2.isAdmin(), user2.getPassword()));
        assertEquals(userDetailsImpl, userDetailsImpl);
    }

    @Test
    public void testAdminMethodViaPublicMethod() {
        builder = new UserDetailsImpl.UserDetailsImplBuilder();
        builder.admin(true);
        UserDetailsImpl userDetails = builder.build();
        assertTrue(userDetails.getAdmin());

        builder.admin(false);
        userDetails = builder.build();
        assertFalse(userDetails.getAdmin());
    }

    @Test
    public void testToString() {
        builder = new UserDetailsImpl.UserDetailsImplBuilder();
        String expectedString = "UserDetailsImpl.UserDetailsImplBuilder(id=null, username=null, firstName=null, lastName=null, admin=null, password=null)";
        assertEquals(expectedString, builder.toString());
    }
}
