package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private final DirectorStorage directorStorage;

    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       DirectorStorage directorStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.directorStorage = directorStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
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
        var newFilm = filmStorage.create(film);
        log.info("Создан новый фильм с id " + newFilm.getId());
        return newFilm;
    }

    public Film update(Film film) throws ValidationException {
        var updatedFilm = filmStorage.update(film);
        log.info("Фильм " + updatedFilm.getId() + " обновлен.");
        return updatedFilm;
    }

    public void removeFilmById(Integer id) {
        log.info("Фильм {} удален", id);
        filmStorage.removeFilmById(id);
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
        filmStorage.addLike(filmId, userId);

        userStorage.createFeed(new Event(new Timestamp(System.currentTimeMillis()).getTime(),
                EventType.LIKE,
                Operation.ADD,
                userId,
                filmId));

        log.info("Добавлен лайк фильму " + filmId + " от пользователя " + userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        if (!filmStorage.deleteLike(filmId, userId)) {
            log.error("Невозможно удалить лайк, не существует фильм id " + filmId + " или пользователь " + userId);
            throw new NullPointerException();
        }

        userStorage.createFeed(new Event(new Timestamp(System.currentTimeMillis()).getTime(),
                EventType.LIKE,
                Operation.REMOVE,
                userId,
                filmId));

        log.info("Лайк фильму " + filmId + " удален.");
    }

    public List<Film> popular(Integer count) {
        var films = findAll();

        TreeSet<Film> filmSortedSet = new TreeSet<>((film1, film2) ->
                film1.getLikes().size() <= film2.getLikes().size() ? 1 : -1);
        filmSortedSet.addAll(films);

        return filmSortedSet.stream().limit(count).collect(Collectors.toList());
    }

    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> getByYearAndLikes(Integer directorId, String value) {
        Director director = directorStorage.get(directorId);
        if (director == null) {
            log.error("Не существует режиссёра с id " + directorId);
            throw new NullPointerException("Не существует режиссёра с id " + directorId);
        }

        if (Objects.equals(value, "year")) {
            return filmStorage.getByDirectorSortedByYear(directorId);
        } else if (Objects.equals(value, "likes")) {
            return filmStorage.getByDirectorSortedByLikes(directorId);
        } else {
            log.error("Неверный параметр sortBy.");
            throw new NullPointerException();
        }
    }

    public List<Film> getPopularFilms(Integer limit, Integer genreId, Integer year) throws ValidationException {
        if (year != null) if (year < 1895) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if (genreId != null) if (genreStorage.get(genreId) == null) {
            log.error("Не существует жанра с id " + genreId);
            throw new NullPointerException("Не существует жанра с id " + genreId);
        }

        if (limit <= 0) {
            log.error("Количество должно быть положительным");
            throw new ValidationException("Количество должно быть положительным");
        }

        return filmStorage.getPopularFilms(limit, genreId, year);
    }
}
