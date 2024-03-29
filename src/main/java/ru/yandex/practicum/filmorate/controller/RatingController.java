package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public List<Rating> findAll() {
        log.info("Запрос на получение списка рейтиногов фильмов.");
        return ratingService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> get(@PathVariable Integer id) throws ValidationException {
        log.info("Получить информацию о рейтинге id " + id);
        return ResponseEntity.ok(ratingService.get(id));
    }
}
