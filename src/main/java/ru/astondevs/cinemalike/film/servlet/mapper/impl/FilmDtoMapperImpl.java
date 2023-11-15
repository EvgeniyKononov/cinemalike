package ru.astondevs.cinemalike.film.servlet.mapper.impl;

import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.servlet.dto.InFilmDto;
import ru.astondevs.cinemalike.film.servlet.dto.OutFilmDto;
import ru.astondevs.cinemalike.film.servlet.mapper.FilmDtoMapper;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.service.GenreService;
import ru.astondevs.cinemalike.genre.service.impl.GenreServiceImpl;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.servlet.dto.OutUserDto;
import ru.astondevs.cinemalike.user.servlet.mapper.UserDtoMapper;
import ru.astondevs.cinemalike.user.servlet.mapper.impl.UserDtoMapperImpl;

import java.util.HashSet;
import java.util.Set;

public class FilmDtoMapperImpl implements FilmDtoMapper {
    private final UserDtoMapper userDtoMapper;
    private final GenreService genreService;

    public FilmDtoMapperImpl() {
        userDtoMapper = new UserDtoMapperImpl();
        genreService = new GenreServiceImpl();
    }

    @Override
    public OutFilmDto map(Film film, Genre genre) {
        String genreName = genre.getName();
        Set<User> users = film.getUserLikes();
        Set<OutUserDto> outUsersDto = userDtoMapper.map(users);
        return new OutFilmDto(film.getName(), outUsersDto, genreName);
    }

    @Override
    public Film toNewEntity(InFilmDto inFilmDto, Genre genre) {
        return new Film(inFilmDto.getName(), genre.getId());
    }

    @Override
    public Set<OutFilmDto> toOutDto(Set<Film> films) {
        Set<OutFilmDto> outFilmsDto = new HashSet<>();
        for (Film film : films) {
            outFilmsDto.add(map(film, genreService.findById(film.getGenre())));
        }
        return outFilmsDto;
    }

}
