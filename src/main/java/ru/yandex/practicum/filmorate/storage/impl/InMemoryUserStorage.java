package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        user.setName(user.getName());
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("User with id {} was created", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("id is required");
        }

        if (users.containsKey(user.getId())) {
            User updatedUser = users.get(user.getId());
            updatedUser.setName(user.getName());
            updatedUser.setLogin(user.getLogin());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setBirthday(user.getBirthday());
            log.info("User with id {} was updated", updatedUser.getId());
            return updatedUser;
        }

        throw new NotFoundException("User with id = " + user.getId() + " not found");
    }

    @Override
    public void delete(int id) {
        if (users.containsKey(id)) users.remove(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Get all users");
        return users.values();
    }

    public User findById(int id) {
        User user = users.get(id);
        if (user == null) throw new NotFoundException("Not found user with id: " + id);
        return user;
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
