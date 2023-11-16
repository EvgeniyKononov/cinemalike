package ru.astondevs.cinemalike.film.repository.impl;

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
import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.genre.repository.GenreRepository;
import ru.astondevs.cinemalike.genre.repository.impl.GenreRepositoryImpl;
import ru.astondevs.cinemalike.like.repository.LikeRepository;
import ru.astondevs.cinemalike.like.repository.impl.LikeRepositoryImpl;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.UserRepository;
import ru.astondevs.cinemalike.user.repository.impl.UserRepositoryImpl;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.astondevs.cinemalike.constant.Constant.DB_CREATION_QUERY;

class FilmRepositoryImplTest {
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
    private FilmRepository filmRepository;
    private GenreRepository genreRepository;
    private Film filmInDb;
    private Film actual;
    private Genre genreInDb;
    private User user1InDb;
    private User user2InDb;

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
        filmRepository = new FilmRepositoryImpl();
        genreRepository = new GenreRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        LikeRepository likeRepository = new LikeRepositoryImpl();
        connectionManager.executeQuery(DB_CREATION_QUERY);
        genreInDb = genreRepository.save(new Genre("action"));
        user1InDb = userRepository.save(new User("sherminator", "sherman"));
        user2InDb = userRepository.save(new User("stifmaster", "stifler"));
        filmInDb = filmRepository.save(new Film("Terminator", genreInDb));
        likeRepository.setLike(user1InDb.getId(), filmInDb.getId());
        likeRepository.setLike(user2InDb.getId(), filmInDb.getId());
    }

    @AfterEach
    void afterEach() {
        connectionManager.executeQuery("drop table users, films, genres, film_likes");
    }

    @Test
    void findById_whenFilmFound_thenReturnFilmWithGenreAndUserLikes() {
        actual = filmRepository.findById(filmInDb.getId());

        assertEquals(filmInDb.getId(), actual.getId());
        assertEquals(filmInDb.getName(), actual.getName());
        assertEquals(genreInDb, actual.getGenre());
        assertTrue(actual.getUserLikes().contains(user1InDb));
        assertTrue(actual.getUserLikes().contains(user2InDb));
    }

    @Test
    void update_whenFilmUpdated_thenReturnFilmWithNewNameAndGenre() {
        Genre newGenreInDb = genreRepository.save(new Genre("Drama"));
        String newName = "Titanic";

        actual = filmRepository.update(new Film(filmInDb.getId(), newName, newGenreInDb));

        assertEquals(filmInDb.getId(), actual.getId());
        assertEquals(newName, actual.getName());
        assertEquals(newGenreInDb, actual.getGenre());
        assertTrue(actual.getUserLikes().contains(user1InDb));
        assertTrue(actual.getUserLikes().contains(user2InDb));
    }

    @Test
    void delete_whenDeleteFilm_thenItNotInDbAndReturn() {
        filmRepository.delete(filmInDb.getId());

        actual = filmRepository.findById(filmInDb.getId());

        assertNull(actual.getId());
        assertNull(actual.getName());
    }

    @Test
    void getLikedFilmsByUserId_whenLikedFilmsInDb_thenReturnAsSet() {
        Set<Film> films = filmRepository.getLikedFilmsByUserId(user1InDb.getId());

        assertEquals(1, films.size());
        Film film = (Film) films.toArray()[0];
        assertEquals(filmInDb, film);
    }
}