package ru.astondevs.cinemalike.user.servlet.mapper.impl;

import ru.astondevs.cinemalike.film.model.Film;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.servlet.dto.InUserDto;
import ru.astondevs.cinemalike.user.servlet.dto.OutUserDto;
import ru.astondevs.cinemalike.user.servlet.mapper.UserDtoMapper;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserDtoMapperImpl implements UserDtoMapper {

    @Override
    public OutUserDto map(User user) {
        Set<String> filmNames = new HashSet<>();
        if (Objects.nonNull(user.getFilmLikes())) {
            for (Film film : user.getFilmLikes()) {
                filmNames.add(film.getName());
            }
        }
        return new OutUserDto(user.getId(), user.getName(), filmNames);
    }

    @Override
    public User map(InUserDto inUserDto) {
        return new User(inUserDto.getLogin(), inUserDto.getName());
    }

    @Override
    public Set<OutUserDto> map(Set<User> users) {
        Set<OutUserDto> outUsersDto = new HashSet<>();
        if (Objects.nonNull(users)) {
            for (User user : users) {
                outUsersDto.add(map(user));
            }
        }
        return outUsersDto;
    }
}
