package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	@Test
	void contextLoads() {
	}

	void init() {

		User user = new User("Maria", "masha@gmail.com", "mashas",
				LocalDate.of(2000,1,1));
		userStorage.create(user);

		Film film = new Film("Avatar", "Film description 1",
				LocalDate.of(2000,1,1), 120, new Rating(1));
		filmStorage.create(film);
	}

	@Test
	public void createUser() {

		User user = new User("Maria", "masha@gmail.com", "mashas",
				LocalDate.of(2000,1,1));
		userStorage.create(user);

		assertTrue(userStorage.findAll().contains(user));
	}

	@Test
	public void testGetUserById() {

		init();

		Optional<User> userOptional = Optional.ofNullable(userStorage.get(1));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	void testUpdateUser() {

		init();

		User user = userStorage.get(1);

		user.setName("Godzilla");
		userStorage.update(user);

		for (User u : userStorage.findAll()) {
			if (u.getId() == 1) {
				assertEquals("Godzilla", u.getName(), "Имя пользователя не обновлено.");
			}
		}
	}

	@Test
	void findAllUsers() {

		init();

		List users = userStorage.findAll();

		assertTrue(users.size() >= 1, "Пользователей в списке должно быть 1 или больше.");
		assertNotNull(users, "Пользователи не найдены.");
	}

	@Test
	public void createFilm() {

		Film film = new Film("AvatarNew", "Film description 1",
				LocalDate.of(2000,1,1), 120, new Rating(2, "PG"));
		filmStorage.create(film);

		var fNames = new ArrayList<>();
		for (Film f : filmStorage.findAll()) {
			fNames.add(f.getName());
		}

		assertTrue(fNames.contains(film.getName()));
	}

	@Test
	public void testGetFilmById() {

		init();

		Optional<Film> filmOptional = Optional.ofNullable(filmStorage.get(1));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	void testUpdateFilm() {

		init();

		Film film = filmStorage.get(1);

		film.setName("Godzilla");
		filmStorage.update(film);

		for (Film f : filmStorage.findAll()) {
			if (f.getId() == 1) {
				assertEquals("Godzilla", f.getName(), "Название фильма не обновлено.");
			}
		}
	}

	@Test
	void findAllFilms() {

		init();

		List films = filmStorage.findAll();

		assertTrue(films.size() >= 1, "Фильмов в списке должно быть 1 или больше.");
		assertNotNull(films, "Фильмы не найдены.");
	}



}
