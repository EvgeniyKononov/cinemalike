package ru.astondevs.cinemalike.film.servlet.mapper.impl;

import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.servlet.dto.InFilmDto;
import ru.astondevs.cinemalike.film.servlet.dto.OutFilmDto;
import ru.astondevs.cinemalike.film.servlet.mapper.FilmDtoMapper;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.servlet.dto.OutUserDto;
import ru.astondevs.cinemalike.user.servlet.mapper.UserDtoMapper;
import ru.astondevs.cinemalike.user.servlet.mapper.impl.UserDtoMapperImpl;

import java.util.HashSet;
import java.util.Set;

public class FilmDtoMapperImpl implements FilmDtoMapper {
    private final UserDtoMapper userDtoMapper;

    public FilmDtoMapperImpl() {
        userDtoMapper = new UserDtoMapperImpl();
    }

    @Override
    public OutFilmDto map(Film film, Genre genre) {
        String genreName = genre.getName();
        Set<User> users = film.getUserLikes();
        Set<OutUserDto> outUsersDto = userDtoMapper.map(users);
        return new OutFilmDto(film.getName(), outUsersDto, genreName);
    }

    @Override
    public OutFilmDto map(Film film) {
        Set<User> users = film.getUserLikes();
        Set<OutUserDto> outUsersDto = userDtoMapper.map(users);
        return new OutFilmDto(film.getName(), outUsersDto, film.getGenre().getName());
    }

    @Override
    public Film toNewEntity(InFilmDto inFilmDto, Genre genre) {
        return new Film(inFilmDto.getName(), genre);
    }

    @Override
    public Set<OutFilmDto> toOutDto(Set<Film> films) {
        Set<OutFilmDto> outFilmsDto = new HashSet<>();
        for (Film film : films) {
            outFilmsDto.add(map(film));
        }
        return outFilmsDto;
    }

}
