package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {

    private int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(min = 1, max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    private LocalDate releaseDate;
    private int duration;

    private Set<Integer> likes;

    public Set<Integer> getLikes() {
        if (likes == null) {
            setLikes(new HashSet<>());
        }
        return likes;
    }

}
