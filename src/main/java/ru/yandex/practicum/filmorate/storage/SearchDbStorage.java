package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class SearchDbStorage implements SearchStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmStorage;

    @Override
    public List<Film> getSearch(String query, String searchBy) {
        String sqlQuery = "SELECT f.*, m.name, g.id, g.name, d.id, d.name " +
                "FROM films AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS likes " +
                "FROM likes GROUP BY film_id) AS l ON f.id = l.film_id " +
                "LEFT JOIN rating AS m ON f.mpa = m.id " +
                "LEFT JOIN films_genres AS fg ON f.id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.id " +
                "LEFT JOIN films_directors AS fd ON f.id = fd.film_id " +
                "LEFT JOIN directors AS d ON fd.director_id = d.id " +
                validateRequest(query, searchBy) +
                "GROUP BY g.id, f.name " +
                "ORDER BY likes DESC";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            return new Film(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDate(4).toLocalDate(),
                    rs.getInt(5),
                    new Rating(rs.getInt(6), rs.getString(7)));
        });
        films.forEach(film -> {
            film.setGenres(filmStorage.getGenresByFilmId(film.getId()));
            film.setDirectors(new HashSet<>(filmStorage.getDirectorsByFilmId(film.getId())));
        });
        return films;
    }

    private String validateRequest(String query, String searchBy) {
        List<String> requestParam = List.of(searchBy.split(","));
        if (requestParam.size() == 1) {
            switch (requestParam.get(0)) {
                case "director":
                    return "WHERE LOWER(d.name) LIKE " + "LOWER('%" + query + "%') ";
                case "title":
                    return "WHERE LOWER(f.name) LIKE " + "LOWER('%" + query + "%') ";
            }
        } else if (requestParam.size() == 2) {
            return "WHERE LOWER(d.name) LIKE " + "LOWER('%" + query + "%') OR LOWER(f.name) LIKE " + "LOWER('%" + query + "%') ";
        } else {
            throw new NotFoundException("Некорректные параметры поиска");
        }
        return "";
    }
}