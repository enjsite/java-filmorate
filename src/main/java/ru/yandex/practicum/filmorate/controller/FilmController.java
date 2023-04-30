package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Получен запрос на получение списка всех фильмов.");
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на создание нового фильма");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на апдейт фильма с id " + film.getId());
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> get(@PathVariable Integer id) throws ValidationException {
        log.info("Запрос на получение фильма с id " + id);
        return ResponseEntity.ok(filmService.get(id));
    }

    @DeleteMapping("/{id}")
    public void removeFilmById(@PathVariable Integer id) {
        log.debug("Получен запрос на удаление фильма номер {}", id);
        filmService.removeFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) throws ValidationException {
        log.info("Получен запрос на добавление лайка фильму с id " + id + " от пользователя " + userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws ValidationException {
        log.info("Получен запрос на удаление лайка фильма с id " + id + " от пользователя " + userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/common")
    public List<Film> commonFilms(@RequestParam Integer userId, @RequestParam Integer friendId) throws ValidationException {
        String message = String.format("Получен запрос вывод общих фильмов с сортировкой по их популярности. " +
                "Id пользователя %d, Id друга %d.", userId, friendId);
        log.info(message);
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/popular")
    public List<Film> popular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен запрос на получение списка " + count + " самых популярных фильмов.");
        return filmService.popular(count);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getByYearAndLikes(@PathVariable Integer directorId, @RequestParam("sortBy") String value) {
        log.info("Получен запрос на получение списка фильмов, сортированному по по количеству лайков или году выпуска.");
        return filmService.getByYearAndLikes(directorId, value);
    }
}
