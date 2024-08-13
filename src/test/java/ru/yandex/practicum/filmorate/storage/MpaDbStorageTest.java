package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class MpaDbStorageTest {

    @Autowired
    private MpaDbStorage mpaDbStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS mpa_type CASCADE");
        jdbcTemplate.execute("CREATE TABLE mpa_type (id INT PRIMARY KEY, name VARCHAR(50))");
        jdbcTemplate.execute("INSERT INTO mpa_type (id, name) VALUES (1, 'G')");
        jdbcTemplate.execute("INSERT INTO mpa_type (id, name) VALUES (2, 'PG')");
        jdbcTemplate.execute("INSERT INTO mpa_type (id, name) VALUES (3, 'PG-13')");
        jdbcTemplate.execute("INSERT INTO mpa_type (id, name) VALUES (4, 'R')");
    }

    @Test
    void getMpaById() {
        Mpa mpa = mpaDbStorage.getMpaById(1);
        assertThat(mpa).isNotNull();
        assertThat(mpa.getId()).isEqualTo(1);
        assertThat(mpa.getName()).isEqualTo("G");
    }

    @Test
    void getMpaByIdNotFound() {
        assertThatThrownBy(() -> mpaDbStorage.getMpaById(999))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Mpa with id 999 not found");
    }

    @Test
    void getAllMpa() {
        List<Mpa> mpas = mpaDbStorage.getAll();
        assertThat(mpas).hasSize(4);
        assertThat(mpas).extracting(Mpa::getName).containsExactlyInAnyOrder("G", "PG", "PG-13", "R");
    }
}
