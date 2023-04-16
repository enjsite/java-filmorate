package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    public Genre(int id) {
        this.id = id;
    }

    private int id;

    private String name;
}
