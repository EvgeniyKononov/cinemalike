package ru.astondevs.cinemalike.film.model;

import ru.astondevs.cinemalike.genre.model.Genre;
import ru.astondevs.cinemalike.user.model.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Film {
    private Long id;
    private String name;
    private Genre genre;
    private Set<User> userLikes;

    public Film() {
        userLikes = new HashSet<>();
    }

    public Film(Long id, String name, Set<User> userLikes, Genre genre) {
        this.id = id;
        this.name = name;
        this.userLikes = userLikes;
        this.genre = genre;
    }

    public Film(String name, Genre genre) {
        this.name = name;
        this.genre = genre;
        userLikes = new HashSet<>();
    }

    public Film(Long id, String name, Genre genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        userLikes = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(Set<User> userLikes) {
        this.userLikes = userLikes;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre=" + genre +
                ", userLikes=" + userLikes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id) && Objects.equals(name, film.name)
                && Objects.equals(genre, film.genre) && Objects.equals(userLikes, film.userLikes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, genre, userLikes);
    }
}
