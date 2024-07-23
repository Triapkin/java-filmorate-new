package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public Film addLike(int filmId, int userId) {
        User user = inMemoryUserStorage.findById(userId);
        Film film = inMemoryFilmStorage.findById(filmId);
        film.getLikes().add(user.getId());
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    public Set<Integer> deleteLikes(int filmId, int userId) {
        User user = inMemoryUserStorage.findById(userId);
        Film film = inMemoryFilmStorage.findById(filmId);
        if (!film.getLikes().contains(user.getId()))
            throw new NotFoundException("User with id:  " + user.getId() + " didn't like the movie with id: " + filmId);
        film.getLikes().remove(user.getId());
        inMemoryFilmStorage.updateFilm(film);
        return film.getLikes();
    }

    public List<Film> getPopular(int count) {
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .collect(Collectors.toList());
    }

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public void delete(int id) {
        inMemoryFilmStorage.deleteFilm(id);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Film addNewFilm(Film film) {
        return inMemoryFilmStorage.addNewFilm(film);
    }
}
