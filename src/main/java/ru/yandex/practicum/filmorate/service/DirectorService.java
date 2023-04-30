package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorStorage directorStorage;

    public Director create(Director director) {
        Director newDirector = directorStorage.create(director);
        log.info("Создан новый фильм с id " + newDirector.getId());
        return newDirector;
    }

    public Director get(Integer id) {
        Director director = directorStorage.get(id);
        if (director == null) {
            log.error("Не существует режиссёра с id " + id);
            throw new NullPointerException("Не существует режиссёра с id " + id);
        }
        return director;
    }

    public Director update(Director director) {
        Director newDirector = directorStorage.update(director);
        log.info("Режиссёр " + newDirector.getId() + " обновлен.");
        return newDirector;
    }

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public void delete(Integer id) {
        directorStorage.delete(id);
    }
}
