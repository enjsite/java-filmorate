package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friendship {

    private int id;

    private int userId;

    private int friendId;

    private boolean isConfirmed;

}
