package ru.astondevs.cinemalike.genre.repository.impl;

import ru.astondevs.cinemalike.db.ConnectionManager;
import ru.astondevs.cinemalike.db.impl.ConnectionManagerImpl;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.FilmRepository;
import ru.astondevs.cinemalike.film.repository.impl.FilmRepositoryImpl;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.repository.GenreRepository;
import ru.astondevs.cinemalike.genre.repository.mapper.GenreResultSetMapper;
import ru.astondevs.cinemalike.genre.repository.mapper.impl.GenreResultSetMapperImpl;

import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import static ru.astondevs.cinemalike.constant.Constant.SQL_EXCEPTION;

public class GenreRepositoryImpl implements GenreRepository {
    private static final Logger log = Logger.getLogger(GenreRepositoryImpl.class.getName());
    private final ConnectionManager connectionManager;
    private final GenreResultSetMapper resultSetMapper;
    private final FilmRepository filmRepository;

    public GenreRepositoryImpl() {
        connectionManager = new ConnectionManagerImpl();
        resultSetMapper = new GenreResultSetMapperImpl();
        filmRepository = new FilmRepositoryImpl();
    }

    @Override
    public Genre findById(Long id, boolean lazy) {
        String query = "SELECT * FROM genres WHERE id = ?";
        Genre genre = new Genre();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            genre = resultSetMapper.map(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        genre.setFilms(getFilms(genre.getId(), lazy));
        return genre;
    }

    @Override
    public Genre findByName(String name) {
        String query = "SELECT * FROM genres WHERE name = ?";
        Genre genre = new Genre();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            genre = resultSetMapper.map(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        genre.setFilms(getFilms(genre.getId(), false));
        return genre;
    }

    @Override
    public Genre save(Genre genre) {
        String query = "INSERT INTO genres (name) VALUES (?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, genre.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return findByName(genre.getName());
    }

    @Override
    public Genre update(Genre genre) {
        String query = "UPDATE genres SET name = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, genre.getName());
            preparedStatement.setLong(2, genre.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return findById(genre.getId(), false);
    }

    @Override
    public boolean delete(Long id) {
        deleteGenreFilmLink(id);
        String query = "DELETE FROM genres WHERE id = ?";
        boolean success = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            success = true;
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return success;
    }

    private Set<Film> getFilms(Long genreId, boolean lazy) {
        Set<Film> films = new HashSet<>();
        if (Objects.nonNull(genreId) && (!lazy)) {
                films = filmRepository.getFilmsByGenreId(genreId);

        }
        return films;
    }
    private void deleteGenreFilmLink(Long id) {
        String query = "UPDATE films SET genre = null WHERE genre = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
    }
}
