package ru.astondevs.cinemalike.user.servlet.dto;

import java.util.Objects;

public class InUserDto {
    private String login;
    private String name;

    public InUserDto() {
    }

    public InUserDto(String login, String name) {
        this.login = login;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InUserDto inUserDto = (InUserDto) o;
        return Objects.equals(login, inUserDto.login) && Objects.equals(name, inUserDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, name);
    }
}
