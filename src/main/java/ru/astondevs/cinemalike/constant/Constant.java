package ru.astondevs.cinemalike.constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Logger;

public class Constant {

    private Constant() {
    }

    private static final Logger log = Logger.getLogger(Constant.class.getName());

    public static final String IO_EXCEPTION = "Got IO Exception ";
    public static final String SQL_EXCEPTION = "Got SQL Exception ";
    public static final String SELECT_ALL_FILMS_JOINED_LIKES = "SELECT * FROM films AS f " +
            "LEFT JOIN film_likes AS fl ON f.id = fl.film_id ";
    public static final String DB_CREATION_QUERY = readSchemaDb();

    private static String readSchemaDb() {
        String query = "";
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File
                        (Objects.requireNonNull(Constant.class.getClassLoader().getResource("schema.sql"))
                                .toURI())))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            query = sb.toString();
        } catch (IOException | URISyntaxException exception) {
            log.severe(SQL_EXCEPTION + exception.getMessage());
        }
        return query;
    }
}
