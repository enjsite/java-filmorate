package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
            return null;
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
    public void delete(Integer id) {

    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        update(user);
        update(friend);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        update(user);
        update(friend);
    }
}
