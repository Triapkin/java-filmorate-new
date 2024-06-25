package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void test_filmWithValidFields() {
        Film film = Film.builder()
                .id(1)
                .name("valid title")
                .description("valid description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Фильм должен быть валидным");
    }

    @Test
    public void test_filmWithInvalidName() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("valid description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Название не должно быть пустым");
    }

    @Test
    public void test_filmWithMaxDescriptionLength() {
        Film film = Film.builder()
                .id(1)
                .name("valid description")
                .description("o".repeat(200))
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Описание фильма должно содержать 200 символов");

        film.setDescription("o".repeat(201));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Описание фильма не может быть больше 200");
    }

    @Test
    public void test_FilmWithNullReleaseDate() {
        Film film = Film.builder()
                .id(1)
                .name("valid name")
                .description("valid description")
                .releaseDate(null)
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Дата релиза не может быть null");
    }

    @Test
    public void test_filmReleaseDateInFuture() {
        Film film = Film.builder()
                .id(1)
                .name("valid name")
                .description("valid description")
                .releaseDate(LocalDate.now().plusDays(1))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Дата релиза не может быть в будущем");
    }

    @Test
    public void test_filmWithNegativeDuration() {
        Film film = Film.builder()
                .id(1)
                .name("valid name")
                .description("valid description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(-120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Продолжительность должна быть позитивным числом");
    }
}
