package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    public HashMap<Integer, Film> films = new HashMap<>();
    public int genId = 0;

    @Override
    public Film get(Integer id) {
        if (!films.containsKey(id)) {
            return null;
        }
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        genId++;
        if (film.getId() == 0) {
            film.setId(genId);
        }
        films.put(film.getId(), film);
        log.info("Film c id " + film.getId() + " добавлен.");
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.info("Film c id " + film.getId() + " обновлен.");
        return film;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void addLike(Film film, Integer userId) {
        film.getLikes().add(userId);
        update(film);
    }

    @Override
    public void deleteLike(Film film, Integer userId) {
        if (!film.getLikes().contains(userId)) {
            log.error("Не существует лайка от пользователя с id " + userId);
            throw new NullPointerException("Не существует лайка от пользователя с id " + userId);
        }
        film.getLikes().remove(userId);
        update(film);
    }
}
