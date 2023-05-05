package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class ReviewService {

    private final ReviewStorage reviewStorage;

    private final UserStorage userStorage;

    private final FilmStorage filmStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage,
                         @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.reviewStorage = reviewStorage;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Review create(Review review) {
        validExistFilmAndUser(review);
        var createdReview =  reviewStorage.create(review);

        userStorage.createFeed(new Event(new Timestamp(System.currentTimeMillis()).getTime(),
                EventType.REVIEW,
                Operation.ADD,
                createdReview.getUserId(),
                createdReview.getId()));

        return createdReview;
    }

    public List<Review> getList(int filmId, int count) {
        if (filmId == -1) {
            return reviewStorage.getList(count);
        } else {
            return reviewStorage.getListByFilmId(filmId, count);
        }
    }

    public Review get(int id) {
        Review review = reviewStorage.get(id);
        if (review == null) {
            throw new NotFoundException("Отзыв с id: " + id + " не найден.");
        }
        return review;
    }

    public Review update(Review review) {
        validExistFilmAndUser(review);

        var updatedReview = reviewStorage.update(review);

        userStorage.createFeed(new Event(new Timestamp(System.currentTimeMillis()).getTime(),
                EventType.REVIEW,
                Operation.UPDATE,
                updatedReview.getUserId(),
                updatedReview.getId()));

        return updatedReview;
    }

    public void deleteById(int id) {
        // Проверка, есть ли что удалять
        if (reviewStorage.get(id) == null) {
            throw new NotFoundException("Отзыв с id: " + id + " не найден.");
        }

        userStorage.createFeed(new Event(new Timestamp(System.currentTimeMillis()).getTime(),
                EventType.REVIEW,
                Operation.REMOVE,
                reviewStorage.get(id).getUserId(),
                id));

        reviewStorage.deleteById(id);
    }

    public void addLike(int reviewId, int userId) {
        validLikeRequest(reviewId, userId);
        reviewStorage.addLike(reviewId, userId);
    }

    public void deleteLike(int reviewId, int userId) {
        validLikeRequest(reviewId, userId);
        reviewStorage.deleteLike(reviewId, userId);
    }

    public void addDisLike(int reviewId, int userId) {
        validLikeRequest(reviewId, userId);
        reviewStorage.addDisLike(reviewId, userId);
    }

    public void deleteDisLike(int reviewId, int userId) {
        validLikeRequest(reviewId, userId);
        reviewStorage.deleteDisLike(reviewId, userId);
    }

    private void validLikeRequest(int reviewId, int userId) {
        //Проверка есть ли такой reviewId
        if (get(reviewId) == null) {
            log.error("Отзыв с id: " + reviewId + " не найден.");
            throw new NotFoundException("Отзыв с id: " + reviewId + " не найден.");
        }
        // Проверка есть ли такой пользователь
        if (userStorage.get(userId) == null) {
            log.error("Пользователь с id: " + userId + " не найден.");
            throw new NotFoundException("Пользователь с id: " + userId + " не найден.");
        }
    }

    private void validExistFilmAndUser(Review review) {
        // Проверка есть ли такой пользователь
        if (userStorage.get(review.getUserId()) == null) {
            log.error("Пользователь с id: " + review.getUserId() + " не найден.");
            throw new NotFoundException("Пользователь с id: " + review.getUserId() + " не найден.");
        }

        // Проверка есть ли такой фильм
        if (filmStorage.get(review.getFilmId()) == null) {
            log.error("Фильм с id " + review.getFilmId() + " не найден.");
            throw new NotFoundException("Фильм с id " + review.getFilmId() + " не найден.");
        }
    }

}
