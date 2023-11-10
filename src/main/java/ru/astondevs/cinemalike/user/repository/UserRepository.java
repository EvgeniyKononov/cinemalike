package ru.astondevs.cinemalike.user.repository;

import ru.astondevs.cinemalike.user.model.User;

public interface UserRepository {
    User findById(Long id);

    User findByLogin(String login);

    User save(User user);

    User update(User user);

    void delete(Long id);
}
