package ru.astondevs.cinemalike.like.repository.impl;

import ru.astondevs.cinemalike.db.ConnectionManager;
import ru.astondevs.cinemalike.db.impl.ConnectionManagerImpl;
import ru.astondevs.cinemalike.like.repository.LikeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import static ru.astondevs.cinemalike.constant.Constant.SQL_EXCEPTION;

public class LikeRepositoryImpl implements LikeRepository {
    private static final Logger log = Logger.getLogger(LikeRepositoryImpl.class.getName());
    private final ConnectionManager connectionManager;

    public LikeRepositoryImpl() {
        connectionManager = new ConnectionManagerImpl();
    }

    @Override
    public boolean setLike(Long userId, Long filmId) {
        String query = "INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)";
        return execute(query, userId, filmId);
    }

    @Override
    public boolean deleteLike(Long userId, Long filmId) {
        String query = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?";
        return execute(query, userId, filmId);
    }

    private boolean execute (String query, Long userId, Long filmId) {
        boolean success = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, filmId);
            preparedStatement.executeUpdate();
            success = true;
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return success;
    }
}
