package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;


public interface ReviewStorage {

    Review create(Review review);

    List<Review> findAll();

    List<Review> getListByFilmId(int filmId, int cnt);

    List<Review> getList(int cnt);

    Review get(int id);

    Review update(Review review);

    void deleteById(int id);

    void addLike(int reviewId, int userId);

    void deleteLike(int reviewId, int userId);

    void addDisLike(int reviewId, int userId);

    void deleteDisLike(int reviewId, int userId);
}
