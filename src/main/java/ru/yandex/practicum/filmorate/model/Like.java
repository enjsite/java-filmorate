package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    private int id;

    private int filmId;

    private int userId;

    public Like(int filmId, int userId) {
        this.filmId = filmId;
        this.userId = userId;
    }
}
