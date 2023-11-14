package ru.astondevs.cinemalike.constant;

public class Constant {

    private Constant() {
    }

    public static final String IO_EXCEPTION = "Got IO Exception ";
    public static final String SQL_EXCEPTION = "Got SQL Exception ";
    public static final String SELECT_ALL_FILMS_JOINED_LIKES = "SELECT * FROM films AS f " +
            "LEFT JOIN film_likes AS fl ON f.id = fl.film_id ";
}
