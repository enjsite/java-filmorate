package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.*;

@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film get(Integer id) {

        String sqlQuery = "SELECT f.id,\n" +
                "       f.name,\n" +
                "       f.description,\n" +
                "       f.release_date,\n" +
                "       f.duration,\n" +
                "       f.mpa,\n" +
                "       r.name\n" +
                "FROM films AS f\n" +
                "JOIN rating AS r ON f.mpa = r.id\n" +
                "WHERE f.id = ?";
        var film = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> new Film(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getDate(4).toLocalDate(),
                rs.getInt(5),
                new Rating(rs.getInt(6), rs.getString(7))
        ), id);

        film.setGenres(getGenresByFilmId(id));
        film.setLikes(getLikesByFilmId(id));

        return film;
    }

    private List<Genre> getGenresByFilmId(int id) {
        String sqlQuery = "SELECT fg.genre_id, g.name" +
                " FROM films_genres AS fg " +
                "JOIN genres as g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?" +
                "ORDER BY g.id ASC ";
        var genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(
                rs.getInt(1),
                rs.getString(2)
        ), id);
        return genres;
    }

    private List<Integer> getLikesByFilmId(int id) {
        String sqlQuery = "SELECT l.user_id" +
                " FROM likes AS l " +
                "WHERE l.film_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt(1), id);
    }

    @Override
    public Film create(Film film) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        var id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(id);

        addGenresToFilm(film);

        return film;
    }

    @Override
    public Film update(Film film) {

        String sqlQuery = "UPDATE films SET " +
                "name = ?, release_date = ?, description = ?, duration = ?, mpa = ? " +
                "where id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", film.getId());

        addGenresToFilm(film);

        return get(film.getId());
    }

    private void addGenresToFilm(Film film) {

        if (film.getGenres().size() > 0) {
            var genresToAdd = new HashSet<Integer>();
            for (Genre genre : film.getGenres()) {
                genresToAdd.add(genre.getId());
            }
            var genresIds = new ArrayList<>(genresToAdd);
            StringBuilder sqlQueryGenres = new StringBuilder("INSERT INTO films_genres (film_id, genre_id)  VALUES");

            for (int i = 0; i < genresIds.size(); i++) {
                sqlQueryGenres.append(" (" + film.getId() + ", " + genresIds.get(i) + ")");
                if (i < genresIds.size() - 1) {
                    sqlQueryGenres.append(",");
                }
            }

            jdbcTemplate.update(sqlQueryGenres.toString());
        }
    }

    @Override
    public List<Film> findAll() {

        // основной запрос
        // фильмы
        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query("SELECT f.id,\n" +
                "       f.name,\n" +
                "       f.description,\n" +
                "       f.release_date,\n" +
                "       f.duration,\n" +
                "       f.mpa,\n" +
                "       r.name\n" +
                "FROM films AS f\n" +
                "JOIN rating AS r ON f.mpa = r.id", (rs, rowNum) -> {

            Film film = new Film(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDate(4).toLocalDate(),
                    rs.getInt(5),
                    new Rating(rs.getInt(6), rs.getString(7)));
            films.put(film.getId(), film);
            return film;
        });

        // жанры
        jdbcTemplate.query("SELECT fg.film_id," +
                "       fg.genre_id," +
                "       g.name " +
                "FROM films_genres fg " +
                "JOIN genres g ON g.id = fg.genre_id", (rs, rowNum) -> {
            Genre genre = new Genre(rs.getInt(2), rs.getString(3));
            int fId = rs.getInt(1);
            films.get(fId).getGenres().add(genre);
            return genre;
        });

        // лайки
        jdbcTemplate.query("SELECT l.user_id, l.film_id FROM likes AS l",
                (rs, rowNum) -> {
                    int like = rs.getInt(1);
                    int fId = rs.getInt(2);
                    films.get(fId).getLikes().add(like);
                    return like;
                });

        return new ArrayList<>(films.values());
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM films WHERE id = ?", id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)", filmId, userId);
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        return jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId) > 0;
    }

    @Override
    public List<Film> getRecommendations(Integer id) {
        String sqlQuery = "SELECT FILM_ID\n" +
                "     FROM LIKES\n" +
                "     WHERE USER_ID IN\n" +
                "         (SELECT id\n" +
                "          FROM\n" +
                "            (SELECT u.id AS id,\n" +
                "                    COUNT(common_likes.cnt) AS cnt2\n" +
                "             FROM USERS u\n" +
                "             LEFT JOIN\n" +
                "               (SELECT USER_ID,\n" +
                "                       COUNT(FILM_ID) AS cnt\n" +
                "                FROM LIKES l\n" +
                "                WHERE (l.FILM_ID IN\n" +
                "                         (SELECT FILM_ID\n" +
                "                          FROM LIKES\n" +
                "                          WHERE USER_ID = ?))\n" +
                "                  AND (USER_ID <> ?)\n" +
                "                GROUP BY USER_ID\n" +
                "                ORDER BY cnt DESC) AS common_likes ON u.ID = common_likes.user_id\n" +
                "             GROUP BY u.id\n" +
                "             ORDER BY cnt2 DESC) AS user_cnt\n" +
                "          WHERE cnt2 =\n" +
                "              (SELECT MAX(CNT2)\n" +
                "               FROM\n" +
                "                 (SELECT u.id AS id,\n" +
                "                         COUNT(common_likes.cnt) AS cnt2\n" +
                "                  FROM USERS u\n" +
                "                  LEFT JOIN\n" +
                "                    (SELECT USER_ID,\n" +
                "                            COUNT(FILM_ID) AS cnt\n" +
                "                     FROM LIKES l\n" +
                "                     WHERE (l.FILM_ID IN\n" +
                "                              (SELECT FILM_ID\n" +
                "                               FROM LIKES\n" +
                "                               WHERE USER_ID = ?))\n" +
                "                       AND (USER_ID <> ?)\n" +
                "                     GROUP BY USER_ID\n" +
                "                     ORDER BY cnt DESC) AS common_likes ON u.ID = common_likes.user_id\n" +
                "                  GROUP BY u.id\n" +
                "                  ORDER BY cnt2 DESC) AS user_cnt))\n" +
                "     EXCEPT SELECT film_id\n" +
                "     FROM LIKES\n" +
                "     WHERE USER_ID = ?";

        List<Integer> filmIds = jdbcTemplate.queryForList(sqlQuery, Integer.class,
                id, id, id, id, id);
        List<Film> films = new ArrayList<>();
        filmIds.forEach(userId -> films.add(get(userId)));
        return films;
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        String sqlReq = "SELECT f.ID FROM FILMS AS f\n" +
                "LEFT JOIN ( SELECT FILM_ID, COUNT(USER_ID) AS cnt FROM LIKES GROUP BY FILM_ID ) AS LIKESCOUNT\n" +
                "ON f.ID = LIKESCOUNT.FILM_ID\n" +
                "WHERE f.ID IN\n" +
                "(SELECT film_id FROM LIKES\n" +
                "WHERE USER_ID = ? AND\n" +
                "FILM_ID IN ( SELECT FILM_ID FROM LIKES WHERE USER_ID = ? ))\n" +
                "ORDER BY LIKESCOUNT.cnt DESC";

        List<Integer> filmIds = jdbcTemplate.queryForList(sqlReq, Integer.class,
                userId, friendId);
        List<Film> films = new ArrayList<>();
        filmIds.forEach(filmId -> films.add(get(filmId)));
        return films;
    }

}
