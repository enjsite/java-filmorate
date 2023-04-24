package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RatingDbStorage implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Rating get(Integer id) {

        String sqlQuery = "SELECT * FROM rating WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> new Rating(
                rs.getInt(1),
                rs.getString(2)
        ), id);
    }

    @Override
    public List<Rating> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM rating", (rs, rowNum) -> new Rating(
                rs.getInt(1),
                rs.getString(2)
        ));
    }
}
