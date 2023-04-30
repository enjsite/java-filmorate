package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {

    Director create(Director director);

    Director get(Integer id);

    Director update(Director director);

    List<Director> getAll();

    void delete(Integer id);
}
