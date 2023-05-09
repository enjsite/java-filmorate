package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private static UserController userController;
    private User user1;
    private User user2;

    @BeforeEach
    void init() throws ValidationException {

        userController = new UserController(new UserService(new InMemoryUserStorage(),
                new InMemoryFilmStorage()));

        user1 = new User(1, "Maria", "masha@gmail.com", "mashas",
                LocalDate.of(2000, 1, 1), null);
        user2 = new User(2, "Ivan", "ivan@gmail.com", "zerrro",
                LocalDate.of(1990, 1, 1), null);

        userController.create(user1);
        userController.create(user2);
    }

    @Test
    void findAll() {

        List users = userController.findAll();

        assertEquals(2, users.size(), "Пользователей в списке должно быть 2.");
        assertNotNull(users, "Пользователи не найдены.");
    }

    @Test
    void create() throws ValidationException {
        User user = new User(3, "Maria", "masha@gmail.com", "mashas",
                LocalDate.of(2000, 1, 1), null);

        userController.create(user);
        assertTrue(userController.findAll().contains(user));
    }

    @Test
    void createFailEmpty() {
        User user = new User();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertFalse(userController.findAll().contains(user));
    }

    @Test
    void createFailLogin() {
        User user = new User(3, "Maria", "masha@gmail.com", "",
                LocalDate.of(2000, 1, 1), null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
        assertFalse(userController.findAll().contains(user));
    }

    @Test
    void update() throws ValidationException {

        user1.setName("Godzilla");
        userController.update(user1);

        for (User user : userController.findAll()) {
            if (user.getId() == 1) {
                assertEquals("Godzilla", user.getName(), "Имя пользователя не обновлено.");
            }
        }
    }

    @Test
    void updateUnknown() {

        User userUpd = new User(9999, "Maria", "masha@gmail.com", "mashas",
                LocalDate.of(2000, 1, 1), null);

        List<Integer> ids = new ArrayList<>();
        for (User user : userController.findAll()) {
            ids.add(user.getId());
        }

        assertFalse(ids.contains(9999));
    }
}