package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDBStorage;

    public Mpa getMpaById(int id) {
        return mpaDBStorage.getMpaById(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaDBStorage.getAll();
    }
}
