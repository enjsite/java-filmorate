package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String name;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Указан невалидный email")
    private String email;

    private String login;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    Set<Integer> friends;

    public Set<Integer> getFriends() {
        if (friends == null) {
            setFriends(new HashSet<>());
        }
        return friends;
    }

}
