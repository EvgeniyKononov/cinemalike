package ru.astondevs.cinemalike.genre.repository.mapper;

import ru.astondevs.cinemalike.genre.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface GenreResultSetMapper {
    Genre map(ResultSet resultSet) throws SQLException;;
}
