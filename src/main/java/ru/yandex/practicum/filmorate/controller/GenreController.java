package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<Genre> findAll() {
        log.info("Запрос на получение списка жанров");
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> get(@PathVariable Integer id) throws ValidationException {
        log.info("Получить информацию о жанре id " + id);
        return ResponseEntity.ok(genreService.get(id));
    }
}
