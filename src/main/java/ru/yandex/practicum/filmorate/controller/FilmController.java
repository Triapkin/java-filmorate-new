package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Get all films");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Add film: {}", film);
        return filmService.addNewFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Update film: {}", newFilm);
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeFilm(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Like film: {}", filmId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Set<Integer> deleteLikes(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Delete likes film: {}", filmId);
        return filmService.deleteLikes(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Get popular films");
        return filmService.getPopular(count);
    }
}
