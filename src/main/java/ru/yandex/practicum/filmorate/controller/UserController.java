package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Get all users");
        return users.values();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
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

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        user.setId(getNextId());
        user.setName(user.getName());
        users.put(user.getId(), user);
        log.info("User with id {} was created", user.getId());
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
