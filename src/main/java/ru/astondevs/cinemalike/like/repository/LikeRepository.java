package ru.astondevs.cinemalike.like.repository;

public interface LikeRepository {
    boolean setLike(Long userId, Long filmId);

    boolean deleteLike(Long userId, Long filmId);
}
