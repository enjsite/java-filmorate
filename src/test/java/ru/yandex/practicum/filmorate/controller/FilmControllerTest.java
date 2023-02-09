package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private static FilmController filmController;
    private Film film1;
    private Film film2;

    @BeforeEach
    void init() throws ValidationException {

        filmController = new FilmController();

        film1 = new Film(1, "Avatar", "Film description 1",
                LocalDate.of(2000,1,1), 120);
        film2 = new Film(2, "Titanic", "Film description 2",
                LocalDate.of(2000,1,1), 120);

        filmController.create(film1);
        filmController.create(film2);
    }

    @Test
    void findAll() {

        List films = filmController.findAll();

        assertEquals(2, films.size(), "Фильмов в списке должно быть 2.");
        assertNotNull(films, "Фильмы не найдены.");
    }

    @Test
    void create() throws ValidationException {
        Film film = new Film(3,"Titanic", "Film description 2",
                LocalDate.of(2000,1,1), 120);

        filmController.create(film);
        assertTrue(filmController.findAll().contains(film));
    }

    @Test
    void createFailName() {
        Film film = new Film(3,"", "Film description 3",
                LocalDate.of(2000,1,1), 120);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Название не может быть пустым", exception.getMessage());
        assertFalse(filmController.findAll().contains(film));
    }

    @Test
    void createFailDescription() {
        Film film = new Film(3,"Test Film", "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
                LocalDate.of(2000,1,1), 120);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
        assertFalse(filmController.findAll().contains(film));
    }

    @Test
    void createFailReleaseDate() {
        Film film = new Film(3,"Test Film", "Description",
                LocalDate.of(1890,1,1), 120);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
        assertFalse(filmController.findAll().contains(film));
    }

    @Test
    void createFailDuration() {
        Film film = new Film(3,"Test Film", "Description",
                LocalDate.of(1998,1,1), -200);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        //assertEquals();
        assertFalse(filmController.findAll().contains(film));
    }

    @Test
    void update() throws ValidationException {

        film1.setName("Godzilla");
        filmController.update(film1);

        for (Film film : filmController.findAll()) {
            if (film.getId() == 1) {
                assertEquals("Godzilla", film.getName(), "Название фильма не обновлено.");
            }
        }
    }

    @Test
    void updateUnknown() throws ValidationException {

        Film filmUpd = new Film(9999, "Avatar", "Film description 1",
                LocalDate.of(2000,1,1), 120);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.update(filmUpd));
        assertEquals("Не существует фильма с id 9999", exception.getMessage());

        List<Integer> ids = new ArrayList<>();
        for (Film film : filmController.findAll()) {
            ids.add(film.getId());
        }

        assertFalse(ids.contains(9999));
    }
}