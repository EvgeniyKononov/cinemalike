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

import java.sql.*;
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
        String query = SELECT_ALL_FILMS_JOINED_LIKES + "LEFT JOIN users AS u ON fl.user_id = u.id WHERE f.id = ?";
        Film film = new Film();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            film = resultSetMapper.map(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        film.setGenre(getGenre(film.getGenre()));
        return film;
    }

    @Override
    public Film findByName(String name) {
        String query = SELECT_ALL_FILMS_JOINED_LIKES + "LEFT JOIN users AS u ON fl.user_id = u.id WHERE f.name = ?";
        Film film = new Film();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            film = resultSetMapper.map(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        film.setGenre(getGenre(film.getGenre()));
        return film;
    }

    @Override
    public Film save(Film film) {
        String query = "INSERT INTO films (name, genre) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, film.getName());
            preparedStatement.setLong(2, film.getGenre().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return findByName(film.getName());
    }

    @Override
    public Film update(Film film) {
        String query = "UPDATE films SET name = ?, genre = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, film.getName());
            preparedStatement.setLong(2, film.getGenre().getId());
            preparedStatement.setLong(3, film.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return findById(film.getId());
    }

    @Override
    public void delete(Long id) {
        deleteUserFilmLink(id);
        String query = "DELETE FROM films WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
    }

    @Override
    public Set<Film> getFilmsByGenreId(Long id) {
        String query = "SELECT * FROM films WHERE genre = ?";
        return getFilms(query, id);
    }

    @Override
    public Set<Film> getLikedFilmsByUserId(Long id) {
        String query = SELECT_ALL_FILMS_JOINED_LIKES + "WHERE fl.user_id = ?";
        return getFilms(query, id);
    }

    private Set<Film> getFilms(String query, Long id) {
        Set<Film> films = new HashSet<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            films = resultSetMapper.toEntity(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        for (Film film : films) {
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

    private void deleteUserFilmLink(Long id) {
        String query = "DELETE FROM film_likes WHERE film_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
    }
}
