package ru.astondevs.cinemalike.genre.repository;

import ru.astondevs.cinemalike.genre.model.Genre;

public interface GenreRepository {
    Genre findById(Long id);

    Genre findByName(String name);

    Genre save(Genre genre);

    Genre update(Genre genre);

    boolean delete(Long id);
}
