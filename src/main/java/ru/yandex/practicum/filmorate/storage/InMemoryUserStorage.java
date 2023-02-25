package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    public HashMap<Integer, User> users = new HashMap<>();
    public int genId = 0;

    @Override
    public User get(Integer id) {
        if (!users.containsKey(id)) {
            log.error("Не существует пользователя с id " + id);
            throw new NullPointerException("Не существует пользователя с id " + id);
        }
        return users.get(id);
    }

    @Override
    public User create(User user) {
        genId++;
        if (user.getId() == 0) {
            user.setId(genId);
        }
        users.put(user.getId(), user);
        log.info("User c id " + user.getId() + " добавлен.");
        return user;
    }

    @Override
    public User update(User user) throws ValidationException {
        if (get(user.getId()) != null) {
            users.put(user.getId(), user);
            log.info("User c id " + user.getId() + " обновлен.");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void addFriend(Integer userId, Integer friendId) throws ValidationException {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        update(user);
        update(friend);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) throws ValidationException {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        update(user);
        update(friend);
    }
}
