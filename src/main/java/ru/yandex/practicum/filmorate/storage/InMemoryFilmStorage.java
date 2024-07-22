package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addNewFilm(Film film) {
        validateFilm(film);
        film.setId(getNextId());
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.info("Added film with id = {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
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

    @Override
    public void deleteFilm(int id) {
        if (films.containsKey(id)) films.remove(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Get all films");
        return films.values();
    }

    public Film findById(int id) {
        if (films.get(id) == null) throw new NotFoundException("Not found film with id: " + id);
        return films.get(id);
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
