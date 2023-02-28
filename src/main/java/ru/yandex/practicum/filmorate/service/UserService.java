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
        return userStorage.create(user);
    }

    public User update(User user) throws ValidationException {
        User curUser = get(user.getId());
        if (curUser != null) {
            validate(user);
            userStorage.update(user);
        }
        return user;
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
        User user = get(userId);
        User friend = get(friendId);
        userStorage.addFriend(user, friend);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = get(userId);
        User friend = get(friendId);
        userStorage.deleteFriend(user, friend);
    }

    public List<User> getFriends(Integer id) {
        User user = get(id);
        List<User> friends = new ArrayList<>();
        for (Integer friendId: user.getFriends()) {
            friends.add(get(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {

        User user = get(id);
        User otherUser = get(otherId);
        List<User> commonFriends = new ArrayList<>();

        Set<Integer> result = user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());

        for (Integer friendId: result) {
            commonFriends.add(get(friendId));
        }

        return commonFriends;
    }

}
