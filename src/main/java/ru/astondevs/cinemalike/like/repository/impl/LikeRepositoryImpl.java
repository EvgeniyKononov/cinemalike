package ru.astondevs.cinemalike.like.repository.impl;

import ru.astondevs.cinemalike.db.ConnectionManager;
import ru.astondevs.cinemalike.db.impl.ConnectionManagerImpl;
import ru.astondevs.cinemalike.like.repository.LikeRepository;

public class LikeRepositoryImpl implements LikeRepository {
    private final ConnectionManager connectionManager;

    public LikeRepositoryImpl() {
        connectionManager = new ConnectionManagerImpl();
    }

    @Override
    public boolean setLike(Long userId, Long filmId) {
        String query = "INSERT INTO film_likes (user_id, film_id) VALUES (" + userId + ", " + filmId + ")";
        return connectionManager.executeQuery(query);
    }

    @Override
    public boolean deleteLike(Long userId, Long filmId) {
        String query = "DELETE FROM film_likes WHERE user_id = " + userId + " AND film_id = " + filmId;
        return connectionManager.executeQuery(query);
    }
}
