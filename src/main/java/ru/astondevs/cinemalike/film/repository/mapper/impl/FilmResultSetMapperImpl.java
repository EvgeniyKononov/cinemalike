package ru.astondevs.cinemalike.film.repository.mapper.impl;

import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.mapper.FilmResultSetMapper;
import ru.astondevs.cinemalike.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class FilmResultSetMapperImpl implements FilmResultSetMapper {

    @Override
    public Film map(ResultSet resultSet) throws SQLException {
        Film film = new Film();
        Set<User> users = new HashSet<>();
        while (resultSet.next()) {
            film.setId(resultSet.getLong(1));
            film.setName(resultSet.getString(2));
            film.setGenre(resultSet.getLong(3));
            User user = new User();
            user.setId(resultSet.getLong(6));
            user.setLogin(resultSet.getString(7));
            user.setName(resultSet.getString(8));
            if(user.getId() != 0) {
                users.add(user);
            }
        }
        film.setUserLikes(users);
        return film;
    }

    @Override
    public Set<Film> toEntity(ResultSet resultSet) throws SQLException {
        Set<Film> films = new HashSet<>();
        while (resultSet.next()) {
            Film film = new Film();
            film.setId(resultSet.getLong(1));
            film.setName(resultSet.getString(2));
            film.setGenre(resultSet.getLong(3));
            films.add(film);
        }
        return films;
    }
}
