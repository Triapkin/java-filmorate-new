package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Get all films");
        return films.values();
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == 0) {
            throw new ValidationException("Film id is required");
        }

        if (films.containsKey(newFilm.getId())) {
            validateFilm(newFilm);
            Film updatedFilm = films.get(newFilm.getId());
            updatedFilm.setName(newFilm.getName());
            updatedFilm.setDescription(newFilm.getDescription());
            updatedFilm.setReleaseDate(newFilm.getReleaseDate());
            updatedFilm.setDuration(newFilm.getDuration());
            log.info("Film with id {} was updated", newFilm.getId());
            return updatedFilm;
        }

        throw new NotFoundException("Film with id = " + newFilm.getId() + " not found");
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Added film with id = {}", film.getId());
        return film;
    }

    private void validateFilm(Film film) {
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Film release date must be after 1985");
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
