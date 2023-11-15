package ru.astondevs.cinemalike.user.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.astondevs.cinemalike.db.ConnectionManager;
import ru.astondevs.cinemalike.db.impl.ConnectionManagerImpl;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.UserRepository;


import static org.junit.jupiter.api.Assertions.*;
import static ru.astondevs.cinemalike.constant.Constant.DB_CREATION_QUERY;

class UserRepositoryImplTest {
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
    private UserRepository repository;
    private User expected;
    private User actual;

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
        repository = new UserRepositoryImpl();
        connectionManager.executeQuery(DB_CREATION_QUERY);
        expected = new User(1L, "sherminator", "sherman");
    }

    @AfterEach
    void afterEach() {
        connectionManager.executeQuery("drop table users, films, genres, film_likes");
    }

    @Test
    void findById_whenUserFound_thenReturnUser() {
        User user = new User(expected.getLogin(), expected.getName());

        repository.save(user);
        actual = repository.findById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    void findById_whenUserNotFound_thenReturnUserWithNullFields() {
        User user = new User(expected.getLogin(), expected.getName());

        repository.save(user);
        actual = repository.findById(2L);

        assertNull(actual.getId());
        assertNull(actual.getLogin());
        assertNull(actual.getName());
    }

    @Test
    void findByLogin_whenUserFound_thenReturnUser() {
        User user = new User(expected.getLogin(), expected.getName());

        repository.save(user);
        actual = repository.findByLogin(expected.getLogin());

        assertEquals(expected, actual);
    }

    @Test
    void findByLogin_whenUserNotFound_thenReturnUserWithNullFields() {
        User user = new User(expected.getLogin(), expected.getName());

        repository.save(user);
        actual = repository.findByLogin("new user");

        assertNull(actual.getId());
        assertNull(actual.getLogin());
        assertNull(actual.getName());
    }

    @Test
    void save_whenSaveUser_thenReturnSavedUser() {
        User user = new User(expected.getLogin(), expected.getName());

        actual = repository.save(user);

        assertEquals(expected, actual);
    }

    @Test
    void save_whenUserNotSavedDueToDuplicate_thenReturnExistingUser() {
        User user = new User(expected.getLogin(), expected.getName());

        repository.save(user);
        user.setName("new name");
        actual = repository.save(user);

        assertEquals(expected, actual);
    }

    @Test
    void update_whenUpdateUser_thenReturnUpdatedUser() {
        User user = new User(expected.getLogin(), expected.getName());

        repository.save(user);
        user.setId(1L);
        user.setName("new name");
        user.setLogin("new login");
        expected.setName("new name");
        expected.setLogin("new login");
        actual = repository.update(user);

        assertEquals(expected, actual);
    }

    @Test
    void delete_whenUserDeleted_thenItNotInDb() {
        User user = new User(expected.getLogin(), expected.getName());

        actual = repository.save(user);
        repository.delete(actual.getId());
        actual = repository.findById(actual.getId());

        assertNull(actual.getId());
        assertNull(actual.getLogin());
        assertNull(actual.getName());
    }
}