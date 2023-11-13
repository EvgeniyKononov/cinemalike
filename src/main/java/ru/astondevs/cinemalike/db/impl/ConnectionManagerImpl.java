package ru.astondevs.cinemalike.db.impl;

import ru.astondevs.cinemalike.db.ConnectionManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

import static ru.astondevs.cinemalike.constant.Constant.IO_EXCEPTION;
import static ru.astondevs.cinemalike.constant.Constant.SQL_EXCEPTION;

public class ConnectionManagerImpl implements ConnectionManager {
    private static final Logger log = Logger.getLogger(ConnectionManagerImpl.class.getName());

    @Override
    public Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try {
            File file = new File(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource("db.yml")).toURI());
            props = getProperties(file);
        } catch (URISyntaxException exception) {
            log.severe("Got URI Syntax Exception " + exception.getMessage());
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        return DriverManager.getConnection(url, username, password);
    }

    public boolean executeQuery(String query) {
        boolean success = false;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
            success = true;
        } catch (SQLException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return success;
    }

    private Properties getProperties(File file) {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(file.toPath())) {
            props.load(in);
        } catch (IOException exception) {
            log.severe(IO_EXCEPTION + exception.getMessage());
        }
        return props;
    }
}

