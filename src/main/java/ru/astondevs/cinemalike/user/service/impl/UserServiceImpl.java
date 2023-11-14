package ru.astondevs.cinemalike.user.service.impl;

import ru.astondevs.cinemalike.exception.DuplicateException;
import ru.astondevs.cinemalike.exception.NotFoundException;
import ru.astondevs.cinemalike.user.model.User;
import ru.astondevs.cinemalike.user.repository.UserRepository;
import ru.astondevs.cinemalike.user.repository.impl.UserRepositoryImpl;
import ru.astondevs.cinemalike.user.service.UserService;

import java.util.Objects;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl() {
        userRepository = new UserRepositoryImpl();
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id);
        if (Objects.isNull(user.getId())) {
            throw new NotFoundException("User with such id not found");
        }
        return user;
    }

    @Override
    public User save(User user) {
        User userInDb = userRepository.findByLogin(user.getLogin());
        if (Objects.nonNull(userInDb.getId())) {
            throw new DuplicateException("User with such login already exist");
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user, User updatedUser) {
        return userRepository.update(new User(user.getId(), updatedUser.getLogin(), updatedUser.getName()));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.delete(id);
    }
}
