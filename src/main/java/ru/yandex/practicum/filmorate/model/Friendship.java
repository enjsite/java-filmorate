package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Friendship {

    private int id;

    private int userId;

    private int friendId;

    private boolean isConfirmed;

}
