package ru.astondevs.cinemalike.film.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.cinemalike.exception.DuplicateException;
import ru.astondevs.cinemalike.exception.NotFoundException;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.FilmRepository;
import ru.astondevs.cinemalike.user.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceImplTest {
    @Mock
    private FilmRepository repository;
    @InjectMocks
    private FilmServiceImpl service;
    private Film expected;
    private Film actual;
    private Long filmId;
    private String filmName;
    private Long userId;
    private Long genreId;
    private Set<User> users;
    private Set<Film> films;

    @BeforeEach
    void setUp() {
        filmId = 1L;
        userId = 2L;
        genreId = 3L;
        filmName = "Terminator";
        users = new HashSet<>(List.of(new User("sherminator", "sherman")));
        films = new HashSet<>();
        expected = new Film(filmId, filmName, users, genreId);
    }

    @Test
    void findById_whenFilmFound_thenReturnFilm() {
        when(repository.findById(filmId)).thenReturn(expected);
        when(repository.getLikedFilmsByUserId(any())).thenReturn(films);

        actual = service.findById(filmId);

        assertEquals(expected, actual);
    }

    @Test
    void findById_whenFilmNotFound_thenThrowNotFoundException() {
        when(repository.findById(filmId)).thenReturn(new Film());

        assertThrows(NotFoundException.class, () -> service.findById(filmId));
    }

    @Test
    void save_whenFilmSaved_thenReturnFilm() {
        when(repository.findByName(filmName)).thenReturn(new Film());
        when(repository.save(expected, genreId)).thenReturn(expected);

        actual = service.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void save_whenFilmAlreadyExist_thenThrowDuplicateException() {
        when(repository.findByName(filmName)).thenReturn(expected);

        assertThrows(DuplicateException.class, () -> service.save(expected));

        verify(repository, never()).save(any(), any());
    }

    @Test
    void update_whenFilmUpdated_thenReturnFilm() {
        when(repository.update(expected, genreId)).thenReturn(expected);

        actual = service.update(expected, expected);

        assertEquals(expected, actual);
    }

    @Test
    void deleteById_whenDeleteFilm_thenInvokeDeletingFromRepository() {
        service.deleteById(filmId);

        verify(repository, only()).delete(filmId);
    }

    @Test
    void findByGenreId_whenFilmsFound_thenReturnFilms() {
        when(repository.getFilmsByGenreId(genreId)).thenReturn(films);

        Set<Film> actual = service.findByGenreId(genreId);

        assertEquals(films, actual);
    }
}