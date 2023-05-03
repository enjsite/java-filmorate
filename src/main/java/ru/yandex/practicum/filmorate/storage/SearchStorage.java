package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface SearchStorage {
    List<Film> getSearch(String query, String searchBy);
}