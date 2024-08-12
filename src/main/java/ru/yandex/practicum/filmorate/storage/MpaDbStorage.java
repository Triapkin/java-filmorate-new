package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MpaDbStorage {

    private static final String SELECT_MPA_BY_ID = "SELECT * FROM mpa_type WHERE id = ?";
    private static final String SELECT_ALL_MPA = "SELECT * FROM mpa_type";

    private final JdbcTemplate jdbc;
    private final MpaRowMapper mapper;

    public Mpa getMpaById(final int id) {
        try {
            return jdbc.queryForObject(SELECT_MPA_BY_ID, mapper, id);
        } catch (final EmptyResultDataAccessException e) {
            throw new NotFoundException("Mpa with id " + id + " not found");
        }
    }

    public List<Mpa> getAll() {
        return jdbc.query(SELECT_ALL_MPA, mapper);
    }
}
