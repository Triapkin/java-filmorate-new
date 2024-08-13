package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY) // Используйте встроенную базу данных H2 для тестов
public class GenreDbStorageTest {

    @Autowired
    private GenreDbStorage genreDbStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS genre_type CASCADE");
        jdbcTemplate.execute("CREATE TABLE genre_type (id INT PRIMARY KEY, name VARCHAR(50))");
        jdbcTemplate.execute("DROP TABLE IF EXISTS film_genres CASCADE");
        jdbcTemplate.execute("CREATE TABLE film_genres (film_id INT, genre_id INT)");
        jdbcTemplate.execute("INSERT INTO genre_type (id, name) VALUES (1, 'Action')");
        jdbcTemplate.execute("INSERT INTO genre_type (id, name) VALUES (2, 'Comedy')");
        jdbcTemplate.execute("INSERT INTO genre_type (id, name) VALUES (3, 'Drama')");
        jdbcTemplate.execute("INSERT INTO genre_type (id, name) VALUES (4, 'Horror')");
    }

    @Test
    void addGenre() {
        genreDbStorage.addGenre(1, List.of(new Genre(1, "Action"), new Genre(2, "Comedy")));
        List<Genre> genres = genreDbStorage.getGenresListForFilm(1);
        assertThat(genres).hasSize(2);
        assertThat(genres).extracting(Genre::getName).containsExactlyInAnyOrder("Action", "Comedy");
    }

    @Test
    void deleteGenresByFilmId() {
        genreDbStorage.addGenre(1, List.of(new Genre(1, "Action"), new Genre(2, "Comedy")));
        genreDbStorage.deleteGenresByFilmId(1);
        List<Genre> genres = genreDbStorage.getGenresListForFilm(1);
        assertThat(genres).isEmpty();
    }

    @Test
    void getGenresListForFilm() {
        genreDbStorage.addGenre(1, List.of(new Genre(1, "Action"), new Genre(2, "Comedy")));
        List<Genre> genres = genreDbStorage.getGenresListForFilm(1);
        assertThat(genres).hasSize(2);
        assertThat(genres).extracting(Genre::getName).containsExactlyInAnyOrder("Action", "Comedy");
    }

    @Test
    void getGenreById() {
        Genre genre = genreDbStorage.getGenreById(1);
        assertThat(genre).isNotNull();
        assertThat(genre.getId()).isEqualTo(1);
        assertThat(genre.getName()).isEqualTo("Action");
    }

    @Test
    void getGenreByIdNotFound() {
        assertThatThrownBy(() -> genreDbStorage.getGenreById(999))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Genre with id 999 not found");
    }

    @Test
    void getAllGenres() {
        List<Genre> genres = genreDbStorage.getAllGenres();
        assertThat(genres).hasSize(4);
        assertThat(genres).extracting(Genre::getName).containsExactlyInAnyOrder("Action", "Comedy", "Drama", "Horror");
    }
}
