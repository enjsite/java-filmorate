package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film get(Integer id) {
        Film film = filmStorage.get(id);
        if (film == null) {
            log.error("Не существует фильма с id " + id);
            throw new NullPointerException("Не существует фильма с id " + id);
        }
        return film;
    }

    public Film create(Film film) throws ValidationException {
        validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {
        Film curFilm = get(film.getId());
        if (curFilm != null) {
            validate(film);
            filmStorage.update(film);
        }
        return film;
    }

    public void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = get(filmId);
        filmStorage.addLike(film, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Film film = get(filmId);
        filmStorage.deleteLike(film, userId);
    }

    public List<Film> popular(Integer count) {
        List<Film> films = findAll();

        TreeSet<Film> filmSortedSet = new TreeSet<>((film1, film2) ->
                film1.getLikes().size() <= film2.getLikes().size() ? 1 : -1);
        filmSortedSet.addAll(films);

        return filmSortedSet.stream().limit(count).collect(Collectors.toList());
    }
}
