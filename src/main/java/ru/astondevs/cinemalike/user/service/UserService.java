package ru.astondevs.cinemalike.user.service;

import ru.astondevs.cinemalike.user.model.User;

public interface UserService {
    User findById(Long id);

    User save(User user);

    User update(User user, User updatedUser);

    void deleteById(Long id);
}
