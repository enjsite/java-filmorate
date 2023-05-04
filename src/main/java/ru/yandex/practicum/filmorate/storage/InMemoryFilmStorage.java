package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public List<Film> getByDirectorSortedByYear(Integer directorId) {
        return null;
    }

    @Override
    public List<Film> getByDirectorSortedByLikes(Integer directorId) {
        return null;
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
    public void removeFilmById(Integer id) {
        films.remove(id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        Film film = get(filmId);
        film.getLikes().add(userId);
        update(film);
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        Film film = get(filmId);
        if (!film.getLikes().contains(userId)) {
            log.error("Не существует лайка от пользователя с id " + userId);
            throw new NullPointerException("Не существует лайка от пользователя с id " + userId);
        }
        var res = film.getLikes().remove(userId);
        update(film);
        return res;
    }

    @Override
    public List<Film> getRecommendations(Integer id) {
        return new ArrayList<>();
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        return new ArrayList<>();
    }

    @Override
    public List<Film> getPopularFilms(Integer limit, Integer genreId, Integer year) {
        return new ArrayList<>();
    }
}
