package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Service
@Slf4j
public class RatingService {

    private final RatingStorage ratingStorage;

    @Autowired
    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public List<Rating> findAll() {
        return ratingStorage.findAll();
    }

    public Rating get(Integer id) {
        Rating rating = ratingStorage.get(id);
        if (rating == null) {
            log.error("Не существует rating с id " + id);
            throw new NullPointerException("Не существует rating с id " + id);
        }
        return rating;
    }
}
