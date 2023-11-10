package ru.astondevs.cinemalike.user.repository.mapper.impl;

import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.mapper.UserResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResulSetMapperImpl implements UserResultSetMapper {
    @Override
    public User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        while(resultSet.next()){
            user.setId(resultSet.getLong("id"));
            user.setLogin(resultSet.getString("login"));
            user.setName(resultSet.getString("name"));
        }
        return user;
    }
}
