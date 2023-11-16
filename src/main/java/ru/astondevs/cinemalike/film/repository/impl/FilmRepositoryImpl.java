package ru.astondevs.cinemalike.film.repository.impl;

import ru.astondevs.cinemalike.db.ConnectionManager;
import ru.astondevs.cinemalike.db.impl.ConnectionManagerImpl;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.FilmRepository;
import ru.astondevs.cinemalike.film.repository.mapper.FilmResultSetMapper;
import ru.astondevs.cinemalike.film.repository.mapper.impl.FilmResultSetMapperImpl;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.repository.GenreRepository;
import ru.astondevs.cinemalike.genre.repository.impl.GenreRepositoryImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import static ru.astondevs.cinemalike.constant.Constant.SELECT_ALL_FILMS_JOINED_LIKES;
import static ru.astondevs.cinemalike.constant.Constant.SQL_EXCEPTION;

public class FilmRepositoryImpl implements FilmRepository {

    private static final Logger log = Logger.getLogger(FilmRepositoryImpl.class.getName());
    private final ConnectionManager connectionManager;
    private final FilmResultSetMapper resultSetMapper;


    public FilmRepositoryImpl() {
        connectionManager = new ConnectionManagerImpl();
        resultSetMapper = new FilmResultSetMapperImpl();
    }

    @Override
    public Film findById(Long id) {
        String query = SELECT_ALL_FILMS_JOINED_LIKES + "LEFT JOIN users AS u ON fl.user_id = u.id WHERE f.id = " + id;
        return getFilm(query);
    }

    @Override
    public Film findByName(String name) {
        String query = SELECT_ALL_FILMS_JOINED_LIKES + "LEFT JOIN users AS u ON fl.user_id = u.id " +
                "WHERE f.name = '" + name + "'";
        return getFilm(query);
    }

    @Override
    public Film save(Film film) {
        String query = "INSERT INTO films (name, genre) " +
                "VALUES ('" + film.getName() + "', '" + film.getGenre().getId() + "')";
        connectionManager.executeQuery(query);
        return findByName(film.getName());
    }

    @Override
    public Film update(Film film) {
        String query = "UPDATE films SET name = '" + film.getName() + "', genre = '" + film.getGenre().getId()
                + "' WHERE id = " + film.getId();
        connectionManager.executeQuery(query);
        return findById(film.getId());
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM film_likes WHERE film_id = " + id;
        connectionManager.executeQuery(query);
        query = "DELETE FROM films WHERE id = " + id;
        connectionManager.executeQuery(query);
    }

    @Override
    public Set<Film> getFilmsByGenreId(Long id) {
        String query = "SELECT * FROM films WHERE genre = " + id;
        return getFilms(query);
    }

    @Override
    public Set<Film> getLikedFilmsByUserId(Long id) {
        String query = SELECT_ALL_FILMS_JOINED_LIKES + "WHERE fl.user_id = " + id;
        return getFilms(query);
    }

    private Film getFilm(String query) {
        Film film = new Film();
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            film = resultSetMapper.map(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        film.setGenre(getGenre(film.getGenre()));
        return film;
    }

    private Set<Film> getFilms(String query) {
        Set<Film> films = new HashSet<>();
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            films = resultSetMapper.toEntity(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        for (Film film : films){
            film.setGenre(getGenre(film.getGenre()));
        }
        return films;
    }

    private Genre getGenre(Genre genre) {
        Genre genreInDb = new Genre();
        if (Objects.nonNull(genre)) {
            GenreRepository genreRepository = new GenreRepositoryImpl();
            boolean lazy = true;
            genreInDb = genreRepository.findById(genre.getId(), lazy);
        }
        return genreInDb;
    }
}
