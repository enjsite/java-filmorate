package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User get(Integer id) {

        String checkQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
        if (jdbcTemplate.queryForObject(checkQuery, Integer.class, id) == 0) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден");
        }

        String sqlQuery = "SELECT " +
                "u.id, " +
                "u.name, " +
                "u.email, " +
                "u.login, " +
                "u.birthday " +
                "FROM users AS u " +
                "WHERE u.id = ?";

        var user = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> new User(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDate(5).toLocalDate()
        ), id);

        return user;
    }

    @Override
    public User create(User user) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        var id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(id);

        return user;
    }

    @Override
    public User update(User user) {

        String sqlQuery = "UPDATE users SET " +
                "login = ?, name = ?, email = ?, birthday = ? " +
                "where id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        return get(user.getId());
    }

    @Override
    public List<User> findAll() {

        var users = jdbcTemplate.query("SELECT u.id,\n" +
                "       u.name,\n" +
                "       u.email,\n" +
                "       u.login,\n" +
                "       u.birthday\n" +
                "FROM users AS u", (rs, rowNum) -> new User(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDate(5).toLocalDate()));

        return users;
    }

    @Override
    public void removeUserById(Integer id) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {

        jdbcTemplate.update("INSERT INTO friendship (user_id, friend_id) " +
                        "VALUES (?, ?)", userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {

        jdbcTemplate.update("DELETE FROM friendship WHERE user_id = ? AND friend_id = ?", userId, friendId);
    }

    @Override
    public List<User> getFriends(Integer id) {

        String checkQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
        if (jdbcTemplate.queryForObject(checkQuery, Integer.class, id) == 0) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден");
        }
        String sqlQuery = "SELECT u.id, u.name, u.email, u.login, u.birthday" +
                " FROM friendship AS fs " +
                "JOIN users as u ON fs.friend_id = u.id " +
                "WHERE fs.user_id = ?";

        var friends = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new User(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDate(5).toLocalDate()), id);

        return friends;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {

        String sqlQuery = "SELECT u.id, u.name, u.email, u.login, u.birthday FROM users u " +
                "WHERE u.id IN " +
                "(SELECT f1.friend_id " +
                "FROM friendship f1 " +
                "WHERE f1.user_id = ? " +
                "INTERSECT " +
                "SELECT f2.friend_id " +
                "FROM friendship f2 " +
                "WHERE f2.user_id = ?)";

        var commonFriends = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new User(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDate(5).toLocalDate()), id, otherId);

        return commonFriends;
    }
}
