package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rating {

    private int id;

    private String name;

    public Rating() {

    }

    public Rating(int id) {
        this.id = id;
    }
}