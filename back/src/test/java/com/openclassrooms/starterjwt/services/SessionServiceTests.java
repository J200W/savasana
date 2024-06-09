package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTests {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private static Session session;
    private static User user;

    @BeforeAll
    static void setUp() {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();
        Teacher teacher = new Teacher(1L, "lastName", "firstName", localDateTime, localDateTime);
        LinkedList<User> users = new LinkedList<>();
        user = new User (1L, "email", "lastName", "firstName", "password", true, localDateTime, localDateTime);
        users.add(new User(1L, "email", "lastName", "firstName", "password", true, localDateTime, localDateTime));

        session = new Session(1L, "name", date, "description", teacher, users, localDateTime, localDateTime);
    }

    @Test
    public void testCreateSession() {
        sessionService.create(session);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testDeleteSession() {
        sessionService.delete(session.getId());
        verify(sessionRepository, times(1)).deleteById(session.getId());
    }

    @Test
    public void testFindAllSessions() {
        sessionService.findAll();
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testFindSessionById() {
        sessionService.getById(session.getId());
        verify(sessionRepository, times(1)).findById(session.getId());
    }

    @Test
    public void testUpdateSession() {
        sessionService.update(session.getId(), session);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testParticipateSession() {

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        sessionService.participate(session.getId(), user.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testAlreadyParticipateSession() {

        session.setUsers(Collections.singletonList(user));

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(session.getId(), user.getId()));
    }

    @Test
    public void testUnParticipateSession() {
        when(sessionRepository.findById(session.getId())).thenReturn(java.util.Optional.of(session));

        sessionService.noLongerParticipate(session.getId(), user.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testAlreadyUnParticipateSession() {
        LocalDateTime localDateTime = LocalDateTime.now();
        User user2 = new User(99L, "email", "lastName", "firstName", "password", true, localDateTime, localDateTime);

        when(sessionRepository.findById(session.getId())).thenReturn(java.util.Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), user2.getId()));
    }

}
