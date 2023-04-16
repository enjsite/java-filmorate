package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre {

    public Genre() {}

    public Genre(int id) {
        this.id = id;
    }

    private int id;

    private String name;
}
