package ru.astondevs.cinemalike.user.repository.mapper;

import ru.astondevs.cinemalike.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserResultSetMapper {
    User map(ResultSet resultSet) throws SQLException;
}
