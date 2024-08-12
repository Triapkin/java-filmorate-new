package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private static final String SELECT_ALL_FILMS = "SELECT * FROM films";
    private static final String DELETE_FILM = "DELETE FROM films WHERE ID = ?";
    private static final String UPDATE_FILM = "UPDATE films SET name=?, description=?, duration=?, releasedate=?,mpa_id=? WHERE id=?";
    private static final String SELECT_FILM_BY_ID = "SELECT * FROM films WHERE id=?";

    private final JdbcTemplate jdbc;
    private final FilmRowMapper mapper;
    private final MpaDbStorage mpaDBStorage;
    private final GenreDbStorage genreDBStorage;

    @Override
    public Film addNewFilm(Film film) {
        validateFilm(film);
        film.setMpa(mpaDBStorage.getMpaById(film.getMpa().getId()));
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc).withTableName("films").usingGeneratedKeyColumns("id");
        Number id = insert.executeAndReturnKey(convertFilmToMap(film));
        film.setId(id.intValue());
        film.setLikes(new HashSet<>());
        List<Genre> genres = genreDBStorage.addGenre(id.intValue(), new ArrayList<>(new HashSet<>(film.getGenres())));
        film.setGenres(genres);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        findById(film.getId());
        jdbc.update(UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        genreDBStorage.deleteGenresByFilmId(id);
        int rows = jdbc.update(DELETE_FILM, id);
        if (rows != 1) throw new NotFoundException("Film with id = " + id + " not found");
    }

    @Override
    public Collection<Film> getAllFilms() {
        return jdbc.query(SELECT_ALL_FILMS, mapper);
    }

    public Film findById(int id) {
        try {
            return jdbc.queryForObject(SELECT_FILM_BY_ID, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Film with id = " + id + " not found");
        }
    }

    private void validateFilm(Film film) {
        if (film.getGenres() == null)
            film.setGenres(new ArrayList<>());
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Film release date must be after 1985");
        try {
            mpaDBStorage.getMpaById(film.getMpa().getId());
            film.getGenres().forEach(g -> genreDBStorage.getGenreById(g.getId()));
        } catch (NotFoundException e) {
            throw new ValidationException("Film does not pass validation");
        }
    }

    private Map<String, Object> convertFilmToMap(Film film) {
        Map<String, Object> filmMap = new HashMap<>();
        filmMap.put("id", film.getId());
        filmMap.put("name", film.getName());
        filmMap.put("description", film.getDescription());
        filmMap.put("releaseDate", film.getReleaseDate());
        filmMap.put("duration", film.getDuration());
        filmMap.put("mpa_id", film.getMpa().getId());
        return filmMap;
    }
}
