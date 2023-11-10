package ru.astondevs.cinemalike.user.model;

import ru.astondevs.cinemalike.film.model.Film;

import java.util.Objects;
import java.util.Set;

public class User {
    private Long id;
    private String login;
    private String name;
    private Set<Film> filmLikes;

    public User() {
    }

    public User(Long id, String login, String name) {
        this.id = id;
        this.login = login;
        this.name = name;
    }

    public User(String login, String name) {
        this.login = login;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Film> getFilmLikes() {
        return filmLikes;
    }

    public void setFilmLikes(Set<Film> filmLikes) {
        this.filmLikes = filmLikes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login) && Objects.equals(name, user.name)
                && Objects.equals(filmLikes, user.filmLikes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, name, filmLikes);
    }
}
