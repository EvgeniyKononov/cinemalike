package ru.astondevs.cinemalike.genre.repository.mapper.impl;

import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.repository.mapper.GenreResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreResultSetMapperImpl implements GenreResultSetMapper {
    @Override
    public Genre map(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        while (resultSet.next()) {
            genre.setId(resultSet.getLong("id"));
            genre.setName(resultSet.getString("name"));
        }
        return genre;
    }
}
