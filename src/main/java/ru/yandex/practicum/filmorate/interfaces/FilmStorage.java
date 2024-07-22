package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film addNewFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int id);

    Collection<Film> getAllFilms();
}
