package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDBStorage;

    public List<Genre> getAllGenres() {
        return genreDBStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return genreDBStorage.getGenreById(id);
    }
}
