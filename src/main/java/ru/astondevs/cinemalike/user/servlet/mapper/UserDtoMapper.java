package ru.astondevs.cinemalike.user.servlet.mapper;

import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.servlet.dto.InUserDto;
import ru.astondevs.cinemalike.user.servlet.dto.OutUserDto;

import java.util.Set;

public interface UserDtoMapper {

    OutUserDto map(User user);
    User map(InUserDto inUserDto);

    Set<OutUserDto> map(Set<User> users);
}
