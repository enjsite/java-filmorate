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
    public List<Review> findAll(@RequestParam(defaultValue = "-1") Integer filmId, @RequestParam(defaultValue = "10") Integer count) {
        return reviewService.getList(filmId, count);
    }

    @GetMapping("/{id}")
    public Review get(@PathVariable Integer id) throws ClassNotFoundException {
        return reviewService.get(id);
    }

    @PostMapping
    public Review create(@Valid @RequestBody Review review) throws ValidationException {
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) throws ValidationException {
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Integer id) throws ClassNotFoundException {
        reviewService.deleteById(id);
        return "Review " + id + " deleted";
    }

    @PutMapping("/{id}/like/{userId}")
    public String addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.addLike(id, userId);
        return "Лайк добавлен";
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.deleteLike(id, userId);
        return "Лайк удален";
    }

    @PutMapping("/{id}/dislike/{userId}")
    public String addDisLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.addDisLike(id, userId);
        return "Дизлайк добавлен";
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public String deleteDisLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.deleteDisLike(id, userId);
        return "Дизлайк удален";
    }

}
