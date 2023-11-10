package ru.astondevs.cinemalike.film.repository.mapper;

import ru.astondevs.cinemalike.film.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public interface FilmResultSetMapper {
    Film map(ResultSet resultSet) throws SQLException;

    Set<Film> toEntity(ResultSet resultSet) throws SQLException;
}
