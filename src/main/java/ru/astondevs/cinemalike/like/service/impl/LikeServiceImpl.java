package ru.astondevs.cinemalike.like.service.impl;

import ru.astondevs.cinemalike.exception.NotFoundException;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.FilmRepository;
import ru.astondevs.cinemalike.film.repository.impl.FilmRepositoryImpl;
import ru.astondevs.cinemalike.like.repository.LikeRepository;
import ru.astondevs.cinemalike.like.repository.impl.LikeRepositoryImpl;
import ru.astondevs.cinemalike.like.service.LikeService;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.UserRepository;
import ru.astondevs.cinemalike.user.repository.impl.UserRepositoryImpl;

import java.util.Objects;

public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    public LikeServiceImpl() {
        likeRepository = new LikeRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        filmRepository = new FilmRepositoryImpl();
    }

    @Override
    public boolean setLike(Long userId, Long filmId) {
        checkUserAndFilmInDb(userId, filmId);
        return likeRepository.setLike(userId, filmId);

    }

    @Override
    public boolean deleteLike(Long userId, Long filmId) {
        checkUserAndFilmInDb(userId, filmId);
        return likeRepository.deleteLike(userId, filmId);

    }

    private void checkUserAndFilmInDb(Long userId, Long filmId) {
        User user = userRepository.findById(userId);
        Film film = filmRepository.findById(filmId);
        if (Objects.isNull(user.getId())) {
            throw new NotFoundException("User with such ID is not exist");
        } else if (Objects.isNull(film.getId())) {
            throw new NotFoundException(("Film with such ID is not exist"));
        }

    }
}
