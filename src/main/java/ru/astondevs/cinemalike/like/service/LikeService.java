package ru.astondevs.cinemalike.like.service;

public interface LikeService {
    boolean setLike(Long userId, Long filmId);

    boolean deleteLike(Long userId, Long filmId);
}
