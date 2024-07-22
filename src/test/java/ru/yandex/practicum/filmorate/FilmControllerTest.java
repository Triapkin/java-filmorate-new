package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private FilmController filmController;
    private FilmService filmService;
    private InMemoryFilmStorage filmStorage;
    private InMemoryUserStorage userStorage;

    private Film film;
    private Film anotherFilm;

    @BeforeEach
    public void setup() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);

        film = Film.builder()
                .id(1)
                .name("Film 1")
                .description("Description 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .likes(new HashSet<>())  // Изменяемое Set
                .build();

        anotherFilm = Film.builder()
                .id(2)
                .name("Film 2")
                .description("Description 2")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(90)
                .likes(new HashSet<>())  // Изменяемое Set
                .build();

        User user1 = User.builder()
                .id(1)
                .email("user1@example.com")
                .login("user1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User user2 = User.builder()
                .id(2)
                .email("user2@example.com")
                .login("user2")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        filmController.addFilm(film);
        filmController.addFilm(anotherFilm);
    }

    @Test
    public void test_GetAllFilms() {
        Collection<Film> result = filmController.getAllFilms();

        assertEquals(2, result.size());
    }

    @Test
    public void test_AddFilm() {
        Film newFilm = Film.builder()
                .id(3)
                .name("New Film")
                .description("New Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(110)
                .likes(new HashSet<>())  // Изменяемое Set
                .build();

        Film result = filmController.addFilm(newFilm);

        assertEquals(newFilm, result);
        assertEquals(3, filmController.getAllFilms().size());
    }

    @Test
    public void test_UpdateFilm() {
        film.setName("Updated Film Name");

        Film result = filmController.updateFilm(film);

        assertEquals("Updated Film Name", result.getName());
    }

    @Test
    public void test_LikeFilm() {
        int userId = 1;
        filmController.likeFilm(film.getId(), userId);

        assertTrue(film.getLikes().contains(userId));
    }

    @Test
    public void test_DeleteLikes() {
        int userId = 1;
        filmController.likeFilm(film.getId(), userId);
        filmController.deleteLikes(film.getId(), userId);

        assertFalse(film.getLikes().contains(userId));
    }

    @Test
    public void test_дGetPopular() {
        filmController.likeFilm(film.getId(), 1);
        filmController.likeFilm(film.getId(), 2);
        filmController.likeFilm(anotherFilm.getId(), 1);

        Collection<Film> result = filmController.getPopular(1);

        assertEquals(1, result.size());
        assertEquals(film, result.iterator().next());
    }
}
