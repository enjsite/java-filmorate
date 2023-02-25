package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    public HashMap<Integer, Film> films = new HashMap<>();
    public int genId = 0;

    @Override
    public Film get(Integer id) {
        if (!films.containsKey(id)) {
            log.error("Не существует фильма с id " + id);
            throw new NullPointerException("Не существует фильма с id " + id);
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
    public Film update(Film film) throws ValidationException {
        if (get(film.getId()) != null) {
            films.put(film.getId(), film);
            log.info("Film c id " + film.getId() + " обновлен.");
        }
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
    public void addLike(Integer filmId, Integer userId) throws ValidationException {
        Film film = get(filmId);
        film.getLikes().add(userId);
        update(film);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) throws ValidationException {
        Film film = get(filmId);
        if (!film.getLikes().contains(userId)) {
            log.error("Не существует лайка от пользователя с id " + userId);
            throw new NullPointerException("Не существует лайка от пользователя с id " + userId);
        }
        film.getLikes().remove(userId);
        update(film);
    }
}
