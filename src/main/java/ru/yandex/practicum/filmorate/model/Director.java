package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Director {

    private int id;

    @NotBlank(message = "Имя режиссёра не может быть пустым")
    private String name;

    public Director(int id) {
        this.id = id;
    }
}