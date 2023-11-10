package ru.astondevs.cinemalike.genre.service;

import ru.astondevs.cinemalike.genre.model.Genre;

public interface GenreService {
    Genre findById(Long id);
    Genre findByName(String name);

    Genre save(Genre genre);

    Genre update(Genre genre, Genre updatedGenre);

    void deleteById(Long id);
}
