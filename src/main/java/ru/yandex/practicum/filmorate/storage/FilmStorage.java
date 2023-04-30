package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film get(Integer id);

    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    void delete(Integer id);

    void addLike(Integer filmId, Integer userId);

    boolean deleteLike(Integer filmId, Integer userId);

    List<Film> getRecommendations(Integer id);

    List<Film> getCommonFilms(Integer userId, Integer friendId);
}
