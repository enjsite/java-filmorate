package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
         return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) throws ValidationException {
        return userService.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) throws ValidationException {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) throws ValidationException {
        return userService.getCommonFriends(id, otherId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) {
        return new ErrorResponse(
                "Ошибка валидации", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final NullPointerException e) {
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

}
