package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User get(Integer id);

    User create(User user);

    User update(User user);

    List<User> findAll();

    void removeUserById(Integer id);

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> getFriends(Integer id);

    List<User> getCommonFriends(Integer id, Integer otherId);

    List<Event> getFeedByUserId(Integer id);

    Event createFeed(Event event);
}
