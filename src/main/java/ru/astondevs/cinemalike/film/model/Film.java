package ru.astondevs.cinemalike.film.model;

import ru.astondevs.cinemalike.user.model.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Film {
    private Long id;
    private String name;
    private Long genreId;
    private Set<User> userLikes;

    public Film() {
        userLikes = new HashSet<>();
    }

    public Film(Long id, String name, Set<User> userLikes, Long genreId) {
        this.id = id;
        this.name = name;
        this.userLikes = userLikes;
        this.genreId = genreId;
    }

    public Film(String name, Long genreId) {
        this.name = name;
        this.genreId = genreId;
        userLikes = new HashSet<>();
    }

    public Film(Long id, String name, Long genreId) {
        this.id = id;
        this.name = name;
        this.genreId = genreId;
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

    public Long getGenre() {
        return genreId;
    }

    public void setGenre(Long genreId) {
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id) && Objects.equals(name, film.name)
                && Objects.equals(userLikes, film.userLikes) && Objects.equals(genreId, film.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userLikes, genreId);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genreId=" + genreId +
                ", userLikes=" + userLikes +
                '}';
    }
}
