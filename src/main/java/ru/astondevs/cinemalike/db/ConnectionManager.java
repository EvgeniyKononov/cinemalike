package ru.astondevs.cinemalike.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManager {
    Connection getConnection() throws SQLException;

    boolean executeQuery(String query);
}
