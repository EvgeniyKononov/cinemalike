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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
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
        String query = "SELECT * FROM genres WHERE id = " + id;
        return getGenre(query, lazy);
    }

    @Override
    public Genre findByName(String name) {
        String query = "SELECT * FROM genres WHERE name = '" + name + "'";
        return getGenre(query, false);
    }

    @Override
    public Genre save(Genre genre) {
        String query = "INSERT INTO genres (name) " +
                "VALUES ('" + genre.getName() + "')";

        connectionManager.executeQuery(query);
        return findByName(genre.getName());
    }

    @Override
    public Genre update(Genre genre) {
        String query = "UPDATE genres SET name = '" + genre.getName() + "' WHERE id = " + genre.getId();
        connectionManager.executeQuery(query);
        return findById(genre.getId(), false);
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE films SET genre = null WHERE genre = " + id;
        connectionManager.executeQuery(query);
        query = "DELETE FROM genres WHERE id = " + id;
        return connectionManager.executeQuery(query);
    }

    private Genre getGenre(String query, boolean lazy) {
        Genre genre = new Genre();
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            genre = resultSetMapper.map(resultSet);
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        if (!lazy) {
            Set<Film> films = filmRepository.getFilmsByGenreId(genre.getId());
            genre.setFilms(films);
        } else {
            genre.setFilms(new HashSet<>());
        }
        return genre;
    }
}
