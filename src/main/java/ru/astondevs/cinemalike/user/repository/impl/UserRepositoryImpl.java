package ru.astondevs.cinemalike.user.repository.impl;

import ru.astondevs.cinemalike.db.ConnectionManager;
import ru.astondevs.cinemalike.db.impl.ConnectionManagerImpl;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.UserRepository;
import ru.astondevs.cinemalike.user.repository.mapper.UserResultSetMapper;
import ru.astondevs.cinemalike.user.repository.mapper.impl.UserResulSetMapperImpl;

import java.sql.*;
import java.util.logging.Logger;

import static ru.astondevs.cinemalike.constant.Constant.IOEXCEPTION;

public class UserRepositoryImpl implements UserRepository {
    private static final Logger log = Logger.getLogger(UserRepositoryImpl.class.getName());
    private final ConnectionManager connectionManager;
    private final UserResultSetMapper resultSetMapper;

    public UserRepositoryImpl() {
        connectionManager = new ConnectionManagerImpl();
        resultSetMapper = new UserResulSetMapperImpl();
    }

    @Override
    public User findById(Long id) {
        String query = "SELECT * FROM users WHERE id = " + id;
        return getUser(query);
    }

    @Override
    public User findByLogin(String login) {
        String query = "SELECT * FROM users WHERE login = '" + login + "'";
        return getUser(query);
    }

    @Override
    public User save(User user) {
        String query = "INSERT INTO users (login, name) " +
                "VALUES ('" + user.getLogin() + "', '" + user.getName() + "')";

        executeQuery(query);
        return findByLogin(user.getLogin());
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET login = '" + user.getLogin() + "', name = '" + user.getName()
                + "' WHERE id = " + user.getId();
        executeQuery(query);
        return findById(user.getId());
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM users WHERE id = " + id;
        executeQuery(query);
    }

    private User getUser(String query) {
        User user = new User();
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            user = resultSetMapper.map(resultSet);
        } catch (SQLException exception) {
            log.severe(IOEXCEPTION + exception.getMessage());
        }
        return user;
    }

    private void executeQuery(String query) {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException exception) {
            log.severe(IOEXCEPTION + exception.getMessage());
        }
    }
}
