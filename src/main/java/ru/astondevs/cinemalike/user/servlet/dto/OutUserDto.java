package ru.astondevs.cinemalike.user.servlet.dto;

import ru.astondevs.cinemalike.film.model.Film;

import java.util.Objects;
import java.util.Set;

public class OutUserDto {
    private Long id;
    private String name;
    private Set<Film> filmLikes;

    public OutUserDto() {
    }

    public OutUserDto(Long id, String name, Set<Film> filmLikes) {
        this.id = id;
        this.name = name;
        this.filmLikes = filmLikes;
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
        OutUserDto that = (OutUserDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(filmLikes, that.filmLikes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, filmLikes);
    }
}
