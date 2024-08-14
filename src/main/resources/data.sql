-- mpa_type data
INSERT INTO mpa_type (name)
SELECT 'G' WHERE NOT EXISTS (SELECT name FROM mpa_type WHERE name = 'G');

INSERT INTO mpa_type (name)
SELECT 'PG' WHERE NOT EXISTS (SELECT name FROM mpa_type WHERE name = 'PG');

INSERT INTO mpa_type (name)
SELECT 'PG-13' WHERE NOT EXISTS (SELECT name FROM mpa_type WHERE name = 'PG-13');

INSERT INTO mpa_type (name)
SELECT 'R' WHERE NOT EXISTS (SELECT name FROM mpa_type WHERE name = 'R');

INSERT INTO mpa_type (name)
SELECT 'NC-17' WHERE NOT EXISTS (SELECT name FROM mpa_type WHERE name = 'NC-17');

-- genre_type data
INSERT INTO genre_type (name)
SELECT 'Комедия' WHERE NOT EXISTS (SELECT name FROM genre_type WHERE name = 'Комедия');

INSERT INTO genre_type (name)
SELECT 'Драма' WHERE NOT EXISTS (SELECT name FROM genre_type WHERE name = 'Драма');

INSERT INTO genre_type (name)
SELECT 'Мультфильм' WHERE NOT EXISTS (SELECT name FROM genre_type WHERE name = 'Мультфильм');

INSERT INTO genre_type (name)
SELECT 'Триллер' WHERE NOT EXISTS (SELECT name FROM genre_type WHERE name = 'Триллер');

INSERT INTO genre_type (name)
SELECT 'Документальный' WHERE NOT EXISTS (SELECT name FROM genre_type WHERE name = 'Документальный');

INSERT INTO genre_type (name)
SELECT 'Боевик' WHERE NOT EXISTS (SELECT name FROM genre_type WHERE name = 'Боевик');

INSERT INTO genre_type (name)
SELECT 'Боевик' WHERE NOT EXISTS (SELECT name FROM genre_type WHERE name = 'Боевик');