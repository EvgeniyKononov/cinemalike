package ru.astondevs.cinemalike.like.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.cinemalike.exception.NotFoundException;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.FilmRepository;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.like.repository.LikeRepository;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FilmRepository filmRepository;
    @InjectMocks
    private LikeServiceImpl service;
    private Long userId;
    private Long filmId;
    private boolean expected;
    private boolean actual;
    private User user;
    private Film film;
    @BeforeEach
    void setUp() {
        userId = 1L;
        filmId = 2L;
        Genre genre = new Genre(3L, "action");
        user = new User(userId, "login", "name");
        film = new Film(filmId, "Terminator", genre);
    }

    @Test
    void setLike_whenSettingLike_thenReturnTrueIfSuccess() {
        expected = true;
        when(userRepository.findById(userId)).thenReturn(user);
        when(filmRepository.findById(filmId)).thenReturn(film);
        when(likeRepository.setLike(userId, filmId)).thenReturn(expected);

        actual = service.setLike(userId, filmId);

        assertEquals(expected, actual);
    }

    @Test
    void setLike_whenUserNotFound_thenThrowNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(new User());

        assertThrows(NotFoundException.class, () -> service.setLike(userId, filmId)) ;

        verify(likeRepository, never()).setLike(userId, filmId);
    }

    @Test
    void setLike_whenFilmNotFound_thenThrowNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(user);
        when(filmRepository.findById(filmId)).thenReturn(new Film());

        assertThrows(NotFoundException.class, () -> service.setLike(userId, filmId)) ;

        verify(likeRepository, never()).setLike(userId, filmId);
    }

    @Test
    void deleteLike_whenDeletingLike_thenReturnTrueIfSuccess() {
        expected = true;
        when(userRepository.findById(userId)).thenReturn(user);
        when(filmRepository.findById(filmId)).thenReturn(film);
        when(likeRepository.deleteLike(userId, filmId)).thenReturn(expected);

        actual = service.deleteLike(userId, filmId);

        assertEquals(expected, actual);
    }
}