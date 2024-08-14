package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class UserDbStorageTest {

    @Autowired
    private UserDbStorage userDbStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS users CASCADE");
        jdbcTemplate.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, email VARCHAR(255), login VARCHAR(255), name VARCHAR(255), birthday DATE)");
        jdbcTemplate.execute("DROP TABLE IF EXISTS friends CASCADE");
        jdbcTemplate.execute("CREATE TABLE friends (user_id BIGINT, friend_id BIGINT, PRIMARY KEY (user_id, friend_id))");
    }


    @Test
    void updateUser() {
        User user = new User(1, "Test User", "test@example.com", "testuser", LocalDate.of(2000, 1, 1), new HashSet<>());
        User createdUser = userDbStorage.addUser(user);

        createdUser.setName("Updated User");
        User updatedUser = userDbStorage.updateUser(createdUser);

        assertThat(updatedUser.getName()).isEqualTo("Updated User");
    }

    @Test
    void deleteUser() {
        User user = new User(2, "Test User", "test@example.com", "testuser", LocalDate.of(2000, 1, 1), new HashSet<>());
        User createdUser = userDbStorage.addUser(user);
        userDbStorage.delete(createdUser.getId());

        assertThatThrownBy(() -> userDbStorage.findById(createdUser.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addAndRemoveFriends() {
        User user1 = new User(3, "User 1", "user1@example.com", "user1", LocalDate.of(2000, 1, 1), new HashSet<>());
        User user2 = new User(4, "User 2", "user2@example.com", "user2", LocalDate.of(2000, 1, 1), new HashSet<>());

        User createdUser1 = userDbStorage.addUser(user1);
        User createdUser2 = userDbStorage.addUser(user2);

        userDbStorage.addFriend(createdUser1.getId(), createdUser2.getId());

        assertThat(userDbStorage.getFriendsByUserId(createdUser1.getId()))
                .contains(createdUser2.getId());

        userDbStorage.deleteFriend(createdUser1.getId(), createdUser2.getId());

        assertThat(userDbStorage.getFriendsByUserId(createdUser1.getId()))
                .doesNotContain(createdUser2.getId());
    }
}
