package ru.astondevs.cinemalike.genre.servlet.dto;

import org.junit.jupiter.api.Test;
import ru.astondevs.cinemalike.film.servlet.dto.OutFilmDto;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OutGenreDtoTest {

    @Test
    void settingMethods_whenCreateObjectsViaConstructorAndViaSetter_thenTheyAreEquals() {
        Set<OutFilmDto> films = new HashSet<>();
        OutGenreDto expected = new OutGenreDto("action", films);
        OutGenreDto actual = new OutGenreDto();
        actual.setName("action");
        actual.setFilms(films);

        assertEquals(expected, actual);
    }

}