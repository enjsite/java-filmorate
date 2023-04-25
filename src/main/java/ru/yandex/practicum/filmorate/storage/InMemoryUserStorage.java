package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    public HashMap<Integer, User> users = new HashMap<>();
    public int genId = 0;

    @Override
    public User get(Integer id) {
        if (!users.containsKey(id)) {
            return null;
        }
        return users.get(id);
    }

    @Override
    public void removeUser(Integer id) {
        log.debug("Пользователь с идентификатором {} удалён.", id);
        if (!users.get(id).getFriends().isEmpty()) {
            for (User friend : users.get(id).getFriends()) {
                friend.getFriends().remove(users.get(id));
            }
        }
        users.remove(id);
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
    public User update(User user)  {
        users.put(user.getId(), user);
        log.info("User c id " + user.getId() + " обновлен.");
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        update(user);
        update(friend);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        update(user);
        update(friend);
    }

    @Override
    public List<User> getFriends(Integer id) {
        User user = get(id);
        List<User> friends = new ArrayList<>();
        for (User friend: user.getFriends()) {
            friends.add(get(friend.getId()));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {

        User user = get(id);
        User otherUser = get(otherId);
        List<User> commonFriends = new ArrayList<>();

        Set<User> result = user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());

        for (User friend: result) {
            commonFriends.add(get(friend.getId()));
        }

        return commonFriends;
    }
}
