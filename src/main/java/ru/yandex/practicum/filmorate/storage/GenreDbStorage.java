package ru.yandex.practicum.filmorate.storage;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre get(Integer id) {

        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> new Genre(
                rs.getInt(1),
                rs.getString(2)
        ), id);
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM genres", (rs, rowNum) -> new Genre(
                rs.getInt(1),
                rs.getString(2)
        ));
    }
}
