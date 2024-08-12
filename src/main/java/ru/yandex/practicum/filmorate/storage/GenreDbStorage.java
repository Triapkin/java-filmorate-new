package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage {

    private static final String SELECT_GENRE_BY_ID = "SELECT * FROM genre_type WHERE id = ?";
    private static final String SELECT_ALL_GENRE = "SELECT * FROM genre_type";
    private static final String SELECT_GENRES_BY_FILM_ID = "SELECT g.* FROM film_genres AS fg JOIN genre_type AS g ON g.id = fg.genre_id WHERE fg.film_id = ?";

    private static final String INSERT_FILM_GENRES = "INSERT INTO film_genres(film_id, genre_id) VALUES(?, ?)";

    private final JdbcTemplate jdbc;
    private final GenreRowMapper mapper;

    public List<Genre> addGenre(int filmId, List<Genre> genres) {
        genres.forEach(genre -> {
            getGenreById(genre.getId());
            jdbc.update(INSERT_FILM_GENRES, filmId, genre.getId());
        });
        return getGenresListForFilm(filmId);
    }

    public void deleteGenresByFilmId(int filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbc.update(sql, filmId);
    }

    public List<Genre> getGenresListForFilm(int filmId) {
        return jdbc.query(SELECT_GENRES_BY_FILM_ID, mapper, filmId);
    }

    public Genre getGenreById(final int id) {
        try {
            return jdbc.queryForObject(SELECT_GENRE_BY_ID, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Genre with id " + id + " not found");
        }
    }

    public List<Genre> getAllGenres() {
        return jdbc.query(SELECT_ALL_GENRE, mapper);
    }
}
