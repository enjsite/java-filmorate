package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public List<Review> findAll(@RequestParam(defaultValue = "-1") Integer filmId,
                                @RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен запрос на получение списка всех отзывов" +
                " с filmId: {} и с count: {}.", filmId, count);
        return reviewService.getList(filmId, count);
    }

    @GetMapping("/{id}")
    public Review get(@PathVariable Integer id) throws ClassNotFoundException {
        log.info("Запрос на получение отзыва с id: {}.", id);
        return reviewService.get(id);
    }

    @PostMapping
    public Review create(@Valid @RequestBody Review review) throws ValidationException {
        log.info("Получен запрос на создание отзыва.");
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) throws ValidationException {
        log.info("Получен запрос на обновление отзыва.");
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        log.info("Получен запрос на удаление отзыва с id: {}.", id);
        return reviewService.deleteById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос на добавление лайка" +
                " для отзыва с id: {} от пользователя с id: {} ", id, userId);
        return reviewService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос на удаление лайка" +
                " для отзыва с id: {} от пользователя с id: {} ", id, userId);
        return reviewService.deleteLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public boolean addDisLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос на добавление дизлайка" +
                " для отзыва с id: {} от пользователя с id: {} ", id, userId);
        return reviewService.addDisLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public boolean deleteDisLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос на удаление дизлайка" +
                " для отзыва с id: {} от пользователя с id: {} ", id, userId);

        return reviewService.deleteDisLike(id, userId);
    }

}
