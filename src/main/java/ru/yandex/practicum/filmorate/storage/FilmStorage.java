package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film get(Integer id) throws ValidationException;

    Film create(Film film);

    Film update(Film film) throws ValidationException;

    List<Film> findAll();

    void delete(Integer id);

    void addLike(Integer filmId, Integer userId) throws ValidationException;

    void deleteLike(Integer filmId, Integer userId) throws ValidationException;
}
