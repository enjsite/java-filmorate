package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User get(Integer id) throws ValidationException;

    User create(User user);

    User update(User user) throws ValidationException;

    List<User> findAll();

    void delete(Integer id);

    void addFriend(Integer userId, Integer friendId) throws ValidationException;

    void deleteFriend(Integer userId, Integer friendId) throws ValidationException;
}
