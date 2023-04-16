package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
