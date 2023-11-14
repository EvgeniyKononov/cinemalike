package ru.astondevs.cinemalike.user.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.cinemalike.exception.DuplicateException;
import ru.astondevs.cinemalike.exception.NotFoundException;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserServiceImpl service;
    private User expected;
    private User actual;
    private Long userId;
    private String userLogin;

    @BeforeEach
    void setUp() {
        userId = 1L;
        userLogin = "sherminator";
        expected = new User(userId, userLogin, "sherman");
    }


    @Test
    void findById_whenUserFound_thenReturnThisUser() {
        when(repository.findById(userId)).thenReturn(expected);

        actual = service.findById(userId);

        assertEquals(expected, actual);
    }

    @Test
    void findById_whenUserNotFound_thenThrowNotFoundException() {
        when(repository.findById(userId)).thenReturn(new User());

        assertThrows(NotFoundException.class,
                () -> service.findById(userId));
    }

    @Test
    void save_whenUserSaved_thenReturnUser() {
        when(repository.findByLogin(userLogin)).thenReturn(new User());
        when(repository.save(expected)).thenReturn(expected);

        actual = service.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void save_whenUserAlreadyExist_thenThrowDuplicateException() {
        when(repository.findByLogin(userLogin)).thenReturn(expected);

        assertThrows(DuplicateException.class,
                () -> service.save(expected));

        verify(repository, never()).save(expected);
    }

    @Test
    void update_whenUpdateUser_thenReturnUpdatedUser() {
        when(repository.update(any())).thenReturn(expected);

        actual = service.update(expected, new User());

        assertEquals(expected, actual);
    }

    @Test
    void deleteById_whenDeleteUser_thenInvokeDeletingFromRepository() {
        service.deleteById(userId);

        verify(repository, only()).delete(userId);
    }
}