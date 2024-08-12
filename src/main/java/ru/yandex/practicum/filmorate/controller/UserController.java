package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Get all users");
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Add user: {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Update user: {}", user);
        return userService.putUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Delete user: {}", id);
        userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Add friend: {}", friendId);
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Delete friend: {}", friendId);
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListFriends(@PathVariable int id) {
        log.info("Get list friends: {}", id);
        return userService.listFriends(id);
    }

    @GetMapping("/{id}/friends/common/{secondId}")
    public List<User> listCommonFriends(@PathVariable int id, @PathVariable int secondId) {
        log.info("List common friends: {}", id);
        return userService.listCommonFriends(id, secondId);
    }
}
