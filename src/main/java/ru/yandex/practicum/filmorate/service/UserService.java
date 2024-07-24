package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public User addFriends(int id, int friendId) {
        if (id == friendId)
            throw new ValidationException("Can't add yourself as a friend");

        User user = inMemoryUserStorage.findById(id);
        user.getFriends().add(friendId);

        User friend = inMemoryUserStorage.findById(friendId);
        friend.getFriends().add(id);

        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);

        return friend;
    }

    public Set<Integer> deleteFriends(int id, int friendId) {
        User user = inMemoryUserStorage.findById(id);
        user.getFriends().remove(friendId);

        User friend = inMemoryUserStorage.findById(friendId);
        friend.getFriends().remove(id);

        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);

        return user.getFriends();
    }

    public List<User> listCommonFriends(int id, int secondId) {
        User user = inMemoryUserStorage.findById(id);
        User secondUser = inMemoryUserStorage.findById(secondId);

        return user.getFriends().stream()
                .filter(friendId -> secondUser.getFriends().contains(friendId))
                .map(inMemoryUserStorage::findById)
                .collect(Collectors.toList());
    }

    public List<User> listFriends(int id) {
        User user = inMemoryUserStorage.findById(id);

        return user.getFriends().stream()
                .map(inMemoryUserStorage::findById)
                .collect(Collectors.toList());
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public void delete(int id) {
        inMemoryUserStorage.delete(id);
    }

    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public User putUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }
}
