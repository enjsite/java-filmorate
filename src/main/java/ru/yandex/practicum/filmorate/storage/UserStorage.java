package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User get(Integer id);

    User create(User user);

    User update(User user);

    List<User> findAll();

    void delete(Integer id);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);
}
