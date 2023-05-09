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

    boolean deleteById(int id);

    boolean addLike(int reviewId, int userId);

    boolean deleteLike(int reviewId, int userId);

    boolean addDisLike(int reviewId, int userId);

    boolean deleteDisLike(int reviewId, int userId);
}
