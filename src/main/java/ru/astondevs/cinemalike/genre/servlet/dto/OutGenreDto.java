package ru.astondevs.cinemalike.genre.servlet.dto;

import ru.astondevs.cinemalike.film.servlet.dto.OutFilmDto;

import java.util.Objects;
import java.util.Set;

public class OutGenreDto {
    private String name;
    private Set<OutFilmDto> films;

    public OutGenreDto() {
    }

    public OutGenreDto(String name) {
        this.name = name;
    }

    public OutGenreDto(String name, Set<OutFilmDto> films) {
        this.name = name;
        this.films = films;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<OutFilmDto> getFilms() {
        return films;
    }

    public void setFilms(Set<OutFilmDto> films) {
        this.films = films;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutGenreDto genreDto = (OutGenreDto) o;
        return Objects.equals(name, genreDto.name) && Objects.equals(films, genreDto.films);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, films);
    }
}
