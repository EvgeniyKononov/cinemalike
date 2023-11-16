package ru.astondevs.cinemalike.user.repository.mapper.impl;

import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.mapper.UserResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserResulSetMapperImpl implements UserResultSetMapper {
    @Override
    public User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        Set<Film> films = new HashSet<>();
        while (resultSet.next()) {
            user.setId(resultSet.getLong(1));
            user.setLogin(resultSet.getString(2));
            user.setName(resultSet.getString(3));
            Film film = new Film();
            film.setId(resultSet.getLong(6));
            film.setName(resultSet.getString(7));
            film.setGenre(new Genre(resultSet.getLong(8), null));
            if (film.getId() != 0) {
                films.add(film);
            }
        }
        user.setFilmLikes(films);
        return user;
    }
}
