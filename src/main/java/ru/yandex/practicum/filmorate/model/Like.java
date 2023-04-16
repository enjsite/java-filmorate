package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    public Like(int filmId, int userId) {
        this.filmId = filmId;
        this.userId = userId;
    }

    private int id;

    private int filmId;

    private int userId;
}
