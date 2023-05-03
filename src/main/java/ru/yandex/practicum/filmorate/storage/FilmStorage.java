package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film get(Integer id);

    List<Film> getByDirectorSortedByYear(Integer directorId);

    List<Film> getByDirectorSortedByLikes(Integer directorId);

    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    void removeFilmById(Integer id);

    void addLike(Integer filmId, Integer userId);

    boolean deleteLike(Integer filmId, Integer userId);

    List<Film> getRecommendations(Integer id);

    List<Film> getCommonFilms(Integer userId, Integer friendId);

    List<Film> getPopularFilms(Integer limit, Integer genreId, Integer year);
}
