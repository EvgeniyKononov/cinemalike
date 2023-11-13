package ru.astondevs.cinemalike.film.repository.impl;

import ru.astondevs.cinemalike.db.ConnectionManager;
import ru.astondevs.cinemalike.db.impl.ConnectionManagerImpl;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.FilmRepository;
import ru.astondevs.cinemalike.film.repository.mapper.FilmResultSetMapper;
import ru.astondevs.cinemalike.film.repository.mapper.impl.FilmResultSetMapperImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

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
        String query = "SELECT * FROM films WHERE id = " + id;
        return getFilm(query);
    }

    @Override
    public Film findByName(String name) {
        String query = "SELECT * FROM films WHERE name = '" + name + "'";
        return getFilm(query);
    }

    @Override
    public Film save(Film film, Long genreId) {
        String query = "INSERT INTO films (name, genre) " +
                "VALUES ('" + film.getName() + "', '" + genreId + "')";
        executeQuery(query);
        return findByName(film.getName());
    }

    @Override
    public Film update(Film film, Long genreId) {
        String query = "UPDATE films SET name = '" + film.getName() + "', genre = '" + genreId
                + "' WHERE id = " + film.getId();
        executeQuery(query);
        return findById(film.getId());
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM films WHERE id = " + id;
        executeQuery(query);
    }

    @Override
    public Set<Film> getFilmsByGenreId(Long id) {
        String query = "SELECT * FROM films WHERE genre = " + id;
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
        return films;
    }

    private void executeQuery(String query) {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
    }
}
