package ru.astondevs.cinemalike.film.repository.mapper.impl;

import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.mapper.FilmResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class FilmResultSetMapperImpl implements FilmResultSetMapper {

    @Override
    public Film map(ResultSet resultSet) throws SQLException {
        Film film = new Film();
        while (resultSet.next()) {
            film.setId(resultSet.getLong("id"));
            film.setName(resultSet.getString("name"));
            film.setGenre(resultSet.getLong("genre"));
        }
        return film;
    }

    @Override
    public Set<Film> toEntity(ResultSet resultSet) throws SQLException {
        Set<Film> films = new HashSet<>();
        while (resultSet.next()) {
            Film film = new Film();
            film.setId(resultSet.getLong("id"));
            film.setName(resultSet.getString("name"));
            film.setGenre(resultSet.getLong("genre"));
            films.add(film);
        }
        return films;
    }
}
