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
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

import static ru.astondevs.cinemalike.constant.Constant.IOEXCEPTION;

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

    private Properties getProperties(File file) {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(file.toPath())) {
            props.load(in);
        } catch (IOException exception) {
            log.severe(IOEXCEPTION + exception.getMessage());
        }
        return props;
    }
}

