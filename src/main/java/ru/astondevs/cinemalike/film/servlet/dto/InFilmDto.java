package ru.astondevs.cinemalike.film.servlet.dto;

import ru.astondevs.cinemalike.genre.servlet.dto.InGenreDto;

import java.util.Objects;

public class InFilmDto {
    private String name;
    private InGenreDto genreDto;

    public InFilmDto() {
    }

    public InFilmDto(String name, InGenreDto genreDto) {
        this.name = name;
        this.genreDto = genreDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InGenreDto getGenreDto() {
        return genreDto;
    }

    public void setGenreDto(InGenreDto genreDto) {
        this.genreDto = genreDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InFilmDto inFilmDto = (InFilmDto) o;
        return Objects.equals(name, inFilmDto.name) && Objects.equals(genreDto, inFilmDto.genreDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, genreDto);
    }
}
