package ru.astondevs.cinemalike.genre.service.impl;

import ru.astondevs.cinemalike.exception.DuplicateException;
import ru.astondevs.cinemalike.exception.NotFoundException;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.repository.GenreRepository;
import ru.astondevs.cinemalike.genre.repository.impl.GenreRepositoryImpl;
import ru.astondevs.cinemalike.genre.service.GenreService;

import java.util.Objects;

public class GenreServiceImpl implements GenreService {
    private GenreRepository genreRepository;

    public GenreServiceImpl() {
        genreRepository = new GenreRepositoryImpl();
    }

    @Override
    public Genre findById(Long id) {
        Genre genre = genreRepository.findById(id);
        if (Objects.isNull(genre.getId())) {
            throw new NotFoundException("Genre with such id not found");
        }
        return genre;
    }

    @Override
    public Genre findByName(String name) {
        Genre genre = genreRepository.findByName(name);
        if (Objects.isNull(genre.getId())) {
            throw new NotFoundException("Genre with such name not found");
        }
        return genre;
    }

    @Override
    public Genre save(Genre genre) {
        Genre genreInDb = genreRepository.findByName(genre.getName());
        if (Objects.nonNull(genreInDb.getId())) {
            throw new DuplicateException("Genre with such name already exist");
        }
        return genreRepository.save(genre);
    }

    @Override
    public Genre update(Genre genre, Genre updatedGenre) {
        return genreRepository.update(new Genre(genre.getId(), updatedGenre.getName()));
    }

    @Override
    public boolean deleteById(Long id) {
        return genreRepository.delete(id);
    }
}
