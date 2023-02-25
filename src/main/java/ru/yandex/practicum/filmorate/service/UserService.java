package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User get(Integer id) throws ValidationException {
        return userStorage.get(id);
    }

    public User create(User user) throws ValidationException {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User user) throws ValidationException {
        if (userStorage.get(user.getId()) == null) {
            log.error("Не существует пользователя с id " + user.getId());
            throw new ValidationException("Не существует пользователя с id " + user.getId());
        }
        validate(user);
        return userStorage.update(user);
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

    public void addFriend(Integer userId, Integer friendId) throws ValidationException {
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) throws ValidationException {
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Integer id) throws ValidationException {

        User user = userStorage.get(id);
        List<User> friends = new ArrayList<>();
        for (Integer friendId: user.getFriends()) {
            friends.add(userStorage.get(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) throws ValidationException {

        User user = userStorage.get(id);
        User otherUser = userStorage.get(otherId);
        List<User> commonFriends = new ArrayList<>();

        Set<Integer> result = user.getFriends().stream()
                .distinct()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());

        for (Integer friendId: result) {
            commonFriends.add(userStorage.get(friendId));
        }

        return commonFriends;
    }

}
