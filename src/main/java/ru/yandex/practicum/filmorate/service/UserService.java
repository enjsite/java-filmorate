package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    private final FilmStorage filmStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User get(Integer id) {
        User user = userStorage.get(id);
        if (user == null) {
            log.error("Не существует пользователя с id " + id);
            throw new NullPointerException("Не существует пользователя с id " + id);
        }
        return user;
    }

    public User create(User user) throws ValidationException {
        validate(user);
        var newUser = userStorage.create(user);
        log.info("Создан новый пользователь с Id " + newUser.getId());
        return newUser;
    }

    public User update(User user) throws ValidationException {
        var updatedUser = userStorage.update(user);
        log.info("Пользователь " + updatedUser.getId() + " обновлен.");
        return updatedUser;
    }

    public void removeUserById(Integer id) {
        log.info("Удаление пользователя {}", id);
        userStorage.removeUserById(id);
    }

    public void validate(User user) throws ValidationException {

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.warn("Имя для отображения может быть пустым — в таком случае будет использован логин;");
            user.setName(user.getLogin());
        }
    }

    public void addFriend(Integer userId, Integer friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Integer id) {
        var friends = userStorage.getFriends(id);
        return friends;
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        var commonFriends = userStorage.getCommonFriends(id, otherId);
        return commonFriends;
    }

    public List<Film> getRecommendations(int userId) {
        return filmStorage.getRecommendations(userId);
    }

}
