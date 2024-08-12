package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class LikeDbStorage {
    private final JdbcTemplate jdbc;

    private static final String SELECT_ALL_LIKES = "select film_id FROM likes group by film_id order by count(user_id) DESC";
    private static final String INSERT_LIKES = "INSERT INTO likes(film_id, user_id) VALUES(?,?)";
    private static final String DELETE_LIKES_BY_FILM_ID = "DELETE FROM likes WHERE film_id=? AND user_id=?";
    private static final String SELECT_LIKES_BY_FILM_ID = "SELECT user_id FROM likes WHERE film_id=?";

    public List<Integer> getFilmsIdByLike() {
        return jdbc.queryForList(SELECT_ALL_LIKES, Integer.class);
    }

    public void addLike(int filmId, int userId) {
        jdbc.update(INSERT_LIKES, filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        jdbc.update(DELETE_LIKES_BY_FILM_ID, filmId, userId);
    }

    public List<Integer> getLikesByFilmId(int filmId) {
        return jdbc.queryForList(SELECT_LIKES_BY_FILM_ID, Integer.class, filmId);
    }
}
