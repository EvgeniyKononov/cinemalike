package ru.astondevs.cinemalike.genre.servlet.dto;

import java.util.Objects;

public class InGenreDto {
    private String name;

    public InGenreDto() {
    }

    public InGenreDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InGenreDto that = (InGenreDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
