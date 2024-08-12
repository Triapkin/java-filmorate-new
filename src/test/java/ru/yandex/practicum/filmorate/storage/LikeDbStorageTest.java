package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class LikeDbStorageTest {

    @Autowired
    private LikeDbStorage likeDbStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS likes");
        jdbcTemplate.execute("CREATE TABLE likes (film_id INT, user_id INT, PRIMARY KEY (film_id, user_id))");
    }

    @Test
    void addLike() {
        likeDbStorage.addLike(1, 100);
        List<Integer> likes = likeDbStorage.getLikesByFilmId(1);
        assertThat(likes).contains(100);
    }

    @Test
    void deleteLike() {
        likeDbStorage.addLike(1, 100);
        likeDbStorage.deleteLike(1, 100);
        List<Integer> likes = likeDbStorage.getLikesByFilmId(1);
        assertThat(likes).doesNotContain(100);
    }

    @Test
    void getLikesByFilmId() {
        likeDbStorage.addLike(1, 100);
        likeDbStorage.addLike(1, 200);
        List<Integer> likes = likeDbStorage.getLikesByFilmId(1);
        assertThat(likes).containsExactlyInAnyOrder(100, 200);
    }

    @Test
    void getFilmsIdByLike() {
        likeDbStorage.addLike(1, 100);
        likeDbStorage.addLike(2, 200);
        List<Integer> filmIds = likeDbStorage.getFilmsIdByLike();
        assertThat(filmIds).containsExactlyInAnyOrder(1, 2);
    }
}
