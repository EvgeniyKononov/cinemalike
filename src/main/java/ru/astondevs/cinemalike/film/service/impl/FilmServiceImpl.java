package ru.astondevs.cinemalike.film.service.impl;

import ru.astondevs.cinemalike.exception.DuplicateException;
import ru.astondevs.cinemalike.exception.NotFoundException;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.FilmRepository;
import ru.astondevs.cinemalike.film.repository.impl.FilmRepositoryImpl;
import ru.astondevs.cinemalike.film.service.FilmService;
import ru.astondevs.cinemalike.genre.repository.GenreRepository;
import ru.astondevs.cinemalike.genre.service.GenreService;
import ru.astondevs.cinemalike.genre.service.impl.GenreServiceImpl;

import java.util.Objects;
import java.util.Set;

public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final GenreService genreService;

    public FilmServiceImpl() {
        filmRepository = new FilmRepositoryImpl();
        genreService = new GenreServiceImpl();
    }

    @Override
    public Film findById(Long id) {
        Film film = filmRepository.findById(id);
        if (Objects.isNull(film.getId())) {
            throw new NotFoundException("Film with such id not found");
        }
        return film;
    }

    @Override
    public Film save(Film film) {
        Film filmInDb = filmRepository.findByName(film.getName());
        if (Objects.nonNull(filmInDb.getId())) {
            throw new DuplicateException("Film with such name already exist");
        }
        return filmRepository.save(film, film.getGenre());
    }

    @Override
    public Film update(Film film, Film updatedFilm) {
        return filmRepository.update(new Film(film.getId(), updatedFilm.getName(), film.getUserLikes(),
                updatedFilm.getGenre()), film.getGenre());
    }

    @Override
    public void deleteById(Long id) {
        filmRepository.delete(id);
    }

    @Override
    public Set<Film> findByGenreId(Long genreId) {
        return filmRepository.getFilmsByGenreId(genreId);
    }
}
