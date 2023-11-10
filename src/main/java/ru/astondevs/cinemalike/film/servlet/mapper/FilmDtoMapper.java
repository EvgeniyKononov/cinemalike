package ru.astondevs.cinemalike.film.servlet.mapper;

import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.servlet.dto.InFilmDto;
import ru.astondevs.cinemalike.film.servlet.dto.OutFilmDto;
import ru.astondevs.cinemalike.genre.model.Genre;

import java.util.Set;

public interface FilmDtoMapper {
    OutFilmDto map(Film film, Genre genre);

    Film toNewEntity(InFilmDto inFilmDto, Genre genre);

    Set<OutFilmDto> toOutDto(Set<Film> films);
}
