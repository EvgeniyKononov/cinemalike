package ru.astondevs.cinemalike.genre.servlet.mapper.impl;

import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.servlet.dto.OutFilmDto;
import ru.astondevs.cinemalike.film.servlet.mapper.FilmDtoMapper;
import ru.astondevs.cinemalike.film.servlet.mapper.impl.FilmDtoMapperImpl;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.service.GenreService;
import ru.astondevs.cinemalike.genre.service.impl.GenreServiceImpl;
import ru.astondevs.cinemalike.genre.servlet.dto.InGenreDto;
import ru.astondevs.cinemalike.genre.servlet.dto.OutGenreDto;
import ru.astondevs.cinemalike.genre.servlet.mapper.GenreDtoMapper;

import java.util.Set;

public class GenreDtoMapperImpl implements GenreDtoMapper {
    private final GenreService genreService;

    public GenreDtoMapperImpl() {
        genreService = new GenreServiceImpl();
    }

    @Override
    public OutGenreDto map(Genre genre, Set<OutFilmDto> films) {
        return new OutGenreDto(genre.getName(), films);
    }

    @Override
    public OutGenreDto map(String name, Set<OutFilmDto> films) {
        Genre genre = genreService.findByName(name);
        return map(genre, films);
    }

    @Override
    public Genre toNewEntity(InGenreDto dto) {
        return new Genre(dto.getName());
    }

    @Override
    public Genre toEntity(InGenreDto dto) {
        return genreService.findByName(dto.getName());
    }
}
