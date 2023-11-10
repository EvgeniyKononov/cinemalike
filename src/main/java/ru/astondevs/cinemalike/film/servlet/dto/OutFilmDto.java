package ru.astondevs.cinemalike.film.servlet.dto;

import ru.astondevs.cinemalike.user.servlet.dto.OutUserDto;

import java.util.Objects;
import java.util.Set;

public class OutFilmDto {
    private String name;
    private Set<OutUserDto> userLikes;
    private String genre;

    public OutFilmDto() {
    }

    public OutFilmDto(String name, Set<OutUserDto> userLikes, String genre) {
        this.name = name;
        this.userLikes = userLikes;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<OutUserDto> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(Set<OutUserDto> userLikes) {
        this.userLikes = userLikes;
    }

    public String getGenreDto() {
        return genre;
    }

    public void setGenreDto(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutFilmDto that = (OutFilmDto) o;
        return Objects.equals(name, that.name) && Objects.equals(userLikes, that.userLikes)
                && Objects.equals(genre, that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, userLikes, genre);
    }
}
