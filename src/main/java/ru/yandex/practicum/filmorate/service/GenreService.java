package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre get(Integer id) {
        Genre genre = genreStorage.get(id);
        if (genre == null) {
            log.error("Не существует жанр с id " + id);
            throw new NullPointerException("Не существует жанр с id " + id);
        }
        return genre;
    }
}
