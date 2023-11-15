package ru.astondevs.cinemalike.genre.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.astondevs.cinemalike.db.ConnectionManager;
import ru.astondevs.cinemalike.db.impl.ConnectionManagerImpl;
import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.film.repository.FilmRepository;
import ru.astondevs.cinemalike.film.repository.impl.FilmRepositoryImpl;
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.repository.GenreRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.astondevs.cinemalike.constant.Constant.DB_CREATION_QUERY;

class GenreRepositoryImplTest {
    private static final int containerPort = 5432;
    private static final int localPort = 5433;
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("cinemalike")
            .withUsername("admin")
            .withPassword("admin")
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(new HostConfig().withPortBindings
                    (new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
            ));
    private static ConnectionManager connectionManager;
    private GenreRepository repository;
    private Genre expected;
    private Genre actual;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        connectionManager = new ConnectionManagerImpl();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        repository = new GenreRepositoryImpl();
        connectionManager.executeQuery(DB_CREATION_QUERY);
        expected = new Genre(1L, "action");
    }

    @AfterEach
    void afterEach() {
        connectionManager.executeQuery("drop table users, films, genres, film_likes");
    }

    @Test
    void findById_whenGenreFound_thenReturnGenre() {
        Genre genre = new Genre(expected.getName());

        repository.save(genre);
        actual = repository.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void findById_whenGenreFoundAndConnectedWithFilm_thenReturnGenreWithFilms() {
        Genre genre = new Genre(expected.getName());
        actual = repository.save(genre);
        Film film1 = new Film("Terminator", actual.getId());
        Film film2 = new Film("Terminator 2", actual.getId());
        FilmRepository filmRepository = new FilmRepositoryImpl();
        filmRepository.save(film1, actual.getId());
        filmRepository.save(film2, actual.getId());
        Set<Film> films = Set.of(filmRepository.findByName(film1.getName()), filmRepository.findByName(film2.getName()));
        expected.setFilms(films);

        actual = repository.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void findById_whenGenreNotFound_thenReturnGenreWithNullFields() {
        Genre genre = new Genre(expected.getName());

        repository.save(genre);
        actual = repository.findById(2L);

        assertNull(actual.getId());
        assertNull(actual.getName());
    }

    @Test
    void findByName_whenGenreFound_thenReturnGenre() {
        Genre genre = new Genre(expected.getName());

        repository.save(genre);
        actual = repository.findByName(genre.getName());

        assertEquals(expected, actual);
    }

    @Test
    void findByName_whenGenreNotFound_thenReturnGenreWithNullFields() {
        Genre genre = new Genre(expected.getName());

        repository.save(genre);
        actual = repository.findByName("new genre");

        assertNull(actual.getId());
        assertNull(actual.getName());
    }

    @Test
    void save_whenGenreSaved_thenReturnSavedGenre() {
        Genre genre = new Genre(expected.getName());

        actual = repository.save(genre);

        assertEquals(expected, actual);
    }

    @Test
    void update_whenGenreUpdated_thenReturnGenre() {
        Genre genre = new Genre(expected.getName());
        String newGenre = "new genre";
        expected.setName(newGenre);

        repository.save(genre);
        genre = new Genre(expected.getId(), newGenre);
        actual = repository.update(genre);

        assertEquals(expected, actual);
    }

    @Test
    void delete_whenDeleteGenre_thenItNotInDbAndReturnTrue() {
        Genre genre = new Genre(expected.getName());
        repository.save(genre);

        boolean success = repository.delete(expected.getId());
        actual = repository.findById(expected.getId());

        assertNull(actual.getId());
        assertNull(actual.getName());
        assertTrue(success);
    }
}