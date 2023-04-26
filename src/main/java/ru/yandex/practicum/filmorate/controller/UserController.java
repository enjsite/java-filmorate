package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
        log.info("Получен запрос на получение списка всех пользователей.");
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос на создание нового пользователя.");
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос на апдейт юзера с id " + user.getId());
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void removeUserById(@PathVariable Integer id) {
        log.info("Запрос на удалени пользователя {}", id);
        userService.removeUserById(id);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) throws ValidationException {
        log.info("Запрос на получение юзера с id " + id);
        return userService.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        log.info("Получен запрос на добавление friendId " + friendId + " юзеру " + id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        log.info("Получен запрос на удаление friendId " + friendId + " у юзера " + id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) throws ValidationException {
        log.info("Получен запрос на получение списка друзей юзера " + id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) throws ValidationException {
        log.info("Получен запрос на получение списка общих друзей юзера " + id + " и " + otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
