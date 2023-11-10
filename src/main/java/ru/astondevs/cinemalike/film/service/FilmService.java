package ru.astondevs.cinemalike.film.service;

import ru.astondevs.cinemalike.film.model.Film;

import java.util.Set;

public interface FilmService {
    Film findById(Long id);

    Film save(Film film);

    Film update(Film film, Film updatedFilm);

    void deleteById(Long id);

    Set<Film> findByGenreId(Long genreId);
}
