package ru.astondevs.cinemalike.genre.servlet.mapper;

import ru.astondevs.cinemalike.film.servlet.dto.OutFilmDto;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.servlet.dto.InGenreDto;
import ru.astondevs.cinemalike.genre.servlet.dto.OutGenreDto;

import java.util.Set;

public interface GenreDtoMapper {
    OutGenreDto map(Genre genre, Set<OutFilmDto> films);
    OutGenreDto map(String genreName, Set<OutFilmDto> films);
    Genre toNewEntity(InGenreDto dto);
    Genre toEntity(InGenreDto dto);
}
