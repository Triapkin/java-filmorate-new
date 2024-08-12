package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDbStorage userDbStorage;

    public User addFriends(int id, int friendId) {
        if (id == friendId)
            throw new ValidationException("Can't add yourself as a friend");
        userDbStorage.addFriend(id, friendId);
        return userDbStorage.findById(id);
    }

    public void deleteFriends(int id, int friendId) {
        userDbStorage.deleteFriend(id, friendId);
    }

    public List<User> listCommonFriends(int id, int secondId) {

        Set<Integer> userFriends = userDbStorage.getFriendsByUserId(id);
        Set<Integer> secondUserFriends = userDbStorage.getFriendsByUserId(secondId);

        return userFriends.stream()
                .filter(secondUserFriends::contains)
                .map(userDbStorage::findById)
                .collect(Collectors.toList());
    }

    public List<User> listFriends(int id) {
        userDbStorage.findById(id);
        Set<Integer> friendIds = userDbStorage.getFriendsByUserId(id);
        return friendIds.stream()
                .map(userDbStorage::findById)
                .collect(Collectors.toList());
    }

    public void delete(int id) {
        userDbStorage.delete(id);
    }

    public User addUser(User user) {
        return userDbStorage.addUser(user);
    }

    public User putUser(User user) {
        return userDbStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }
}
