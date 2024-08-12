package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;
    private UserService userService;

    private User user;
    private User friend;

    @BeforeEach
    public void setup() {
        userController = new UserController(userService);

        user = User.builder()
                .id(1)
                .email("user@example.com")
                .login("userlogin")
                .name("Username")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new HashSet<>())
                .build();

        friend = User.builder()
                .id(2)
                .email("friend@example.com")
                .login("friendlogin")
                .name("Friend")
                .birthday(LocalDate.of(1991, 2, 2))
                .friends(new HashSet<>())
                .build();

        userController.addUser(user);
        userController.addUser(friend);
    }

    @Test
    public void test_GetAllUsers() {
        Collection<User> result = userController.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    public void testAddUser() {
        User newUser = User.builder()
                .id(3)
                .email("newuser@example.com")
                .login("newuserlogin")
                .name("New User")
                .birthday(LocalDate.of(2000, 1, 1))
                .friends(new HashSet<>())
                .build();

        User result = userController.addUser(newUser);

        assertEquals(newUser, result);
        assertEquals(3, userController.getAllUsers().size());
    }

    @Test
    public void test_PutUser() {
        user.setName("Updated Username");

        User result = userController.updateUser(user);

        assertEquals("Updated Username", result.getName());
    }

    @Test
    public void test_AddFriends() {
        User result = userController.addFriends(user.getId(), friend.getId());

        assertTrue(user.getFriends().contains(friend.getId()));
        assertTrue(friend.getFriends().contains(user.getId()));
        assertEquals(friend, result);
    }

    @Test
    public void test_DeleteFriends() {
        userController.addFriends(user.getId(), friend.getId());
        assertFalse(user.getFriends().contains(friend.getId()));
        assertFalse(friend.getFriends().contains(user.getId()));
    }

    @Test
    public void test_GetListFriends() {
        userController.addFriends(user.getId(), friend.getId());

        List<User> result = userController.getListFriends(user.getId());

        assertEquals(1, result.size());
        assertEquals(friend, result.get(0));
    }

    @Test
    public void test_ListCommonFriends() {
        User commonFriend = User.builder()
                .id(3)
                .email("commonfriend@example.com")
                .login("commonfriendlogin")
                .name("Common Friend")
                .birthday(LocalDate.of(1995, 5, 5))
                .friends(new HashSet<>())
                .build();

        userController.addUser(commonFriend);
        userController.addFriends(user.getId(), commonFriend.getId());
        userController.addFriends(friend.getId(), commonFriend.getId());

        List<User> result = userController.listCommonFriends(user.getId(), friend.getId());

        assertEquals(1, result.size());
        assertEquals(commonFriend, result.get(0));
    }
}

