package ru.astondevs.cinemalike.film.repository;

import ru.astondevs.cinemalike.film.model.Film;

import java.util.Set;

public interface FilmRepository {
    Film findById(Long id);

    Film findByName(String name);

    Film save(Film film);

    Film update(Film film);

    void delete(Long id);

    Set<Film> getFilmsByGenreId(Long id);

    Set<Film> getLikedFilmsByUserId(Long id);
}
