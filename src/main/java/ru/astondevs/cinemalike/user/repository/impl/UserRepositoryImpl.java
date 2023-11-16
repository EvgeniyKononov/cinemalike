package ru.astondevs.cinemalike.user.repository.impl;

import ru.astondevs.cinemalike.db.ConnectionManager;
import ru.astondevs.cinemalike.db.impl.ConnectionManagerImpl;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.UserRepository;
import ru.astondevs.cinemalike.user.repository.mapper.UserResultSetMapper;
import ru.astondevs.cinemalike.user.repository.mapper.impl.UserResulSetMapperImpl;

import java.sql.*;
import java.util.logging.Logger;

import static ru.astondevs.cinemalike.constant.Constant.*;

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
        String query = "SELECT * FROM users AS u LEFT JOIN film_likes AS fl ON u.id = fl.user_id " +
                "LEFT JOIN films AS f ON fl.film_id = f.id WHERE u.id = ?";
        User user = new User();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            user = resultSetMapper.map(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return user;
    }

    @Override
    public User findByLogin(String login) {
        String query = "SELECT * FROM users AS u LEFT JOIN film_likes AS fl ON u.id = fl.user_id " +
                "LEFT JOIN films AS f ON fl.film_id = f.id WHERE u.login = ?";
        User user = new User();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            user = resultSetMapper.map(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return user;
    }

    @Override
    public User save(User user) {
        String query = "INSERT INTO users (login, name) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return findByLogin(user.getLogin());
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET login = ?, name = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return findById(user.getId());
    }

    @Override
    public void delete(Long id) {
        deleteUserFilmLink(id);
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
    }

    private void deleteUserFilmLink(Long id) {
        String query = "DELETE FROM film_likes WHERE user_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
    }
}
