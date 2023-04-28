package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review create(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("id");

        int newId = simpleJdbcInsert.executeAndReturnKey(review.toMap()).intValue();

        review.setId(newId);

        log.info("Успшено сохранен отзыв с id: {}.", review.getId());

        return get(newId);
    }

    @Override
    public List<Review> findAll() {
        String sqlQuery = "SELECT reviews.id AS id,\n" +
                "       reviews.content AS content,\n" +
                "       reviews.is_positive AS is_positive,\n" +
                "       reviews.user_id AS user_id,\n" +
                "       reviews.film_id AS film_id,\n" +
                "       SUM(CASE\n" +
                "               WHEN review_likes.is_like = TRUE THEN 1\n" +
                "               WHEN review_likes.is_like = FALSE THEN -1\n" +
                "               ELSE 0\n" +
                "           END) AS useful\n" +
                "FROM REVIEWS\n" +
                "LEFT JOIN review_likes ON reviews.id = review_likes.review_id\n" +
                "GROUP BY reviews.id\n" +
                "ORDER BY useful DESC";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeReview(rs));
    }


    @Override
    public List<Review> getListByFilmId(int filmId, int cnt) {
        String sqlQuery = "SELECT reviews.id AS id,\n" +
                "       reviews.content AS content,\n" +
                "       reviews.is_positive AS is_positive,\n" +
                "       reviews.user_id AS user_id,\n" +
                "       reviews.film_id AS film_id,\n" +
                "       SUM(CASE\n" +
                "               WHEN review_likes.is_like = TRUE THEN 1\n" +
                "               WHEN review_likes.is_like = FALSE THEN -1\n" +
                "               ELSE 0\n" +
                "           END) AS useful\n" +
                "FROM REVIEWS\n" +
                "LEFT JOIN review_likes ON reviews.id = review_likes.review_id\n" +
                "WHERE reviews.FILM_ID = ? \n" +
                "GROUP BY reviews.id\n" +
                "ORDER BY useful DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> makeReview(rs), filmId, cnt);

    }

    @Override
    public List<Review> getList(int cnt) {
        String sqlQuery = "SELECT reviews.id AS id,\n" +
                "       reviews.content AS content,\n" +
                "       reviews.is_positive AS is_positive,\n" +
                "       reviews.user_id AS user_id,\n" +
                "       reviews.film_id AS film_id,\n" +
                "       SUM(CASE\n" +
                "               WHEN review_likes.is_like = TRUE THEN 1\n" +
                "               WHEN review_likes.is_like = FALSE THEN -1\n" +
                "               ELSE 0\n" +
                "           END) AS useful\n" +
                "FROM REVIEWS\n" +
                "LEFT JOIN review_likes ON reviews.id = review_likes.review_id\n" +
                "GROUP BY reviews.id\n" +
                "ORDER BY useful DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> makeReview(rs), cnt);

    }

    @Override
    public Review get(int id) {
        String sqlQuery = "SELECT reviews.id AS id,\n" +
                "       reviews.content AS content,\n" +
                "       reviews.is_positive AS is_positive,\n" +
                "       reviews.user_id AS user_id,\n" +
                "       reviews.film_id AS film_id,\n" +
                "       SUM(CASE\n" +
                "               WHEN review_likes.is_like = TRUE THEN 1\n" +
                "               WHEN review_likes.is_like = FALSE THEN -1\n" +
                "               ELSE 0\n" +
                "           END) AS useful\n" +
                "FROM REVIEWS\n" +
                "LEFT JOIN review_likes ON reviews.id = review_likes.review_id\n" +
                "WHERE reviews.id = ?\n" +
                "GROUP BY reviews.id";

        List<Review> reviews = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> makeReview(rs), id);

        if (reviews.isEmpty()) {
            return null;
        }

        return reviews.get(0);
    }


    @Override
    public Review update(Review review) {

        String sqlQuery = "UPDATE reviews SET " +
                "content = ?, is_positive = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery, review.getContent(),
                review.getIsPositive(), review.getId());

        log.info("Успшено обновлен отзыв с id: {}.", review.getId());

        return get(review.getId());
    }

    @Override
    public void deleteById(int id) {
        String sqlQuery = "DELETE FROM reviews WHERE id = ?";

        jdbcTemplate.update(sqlQuery, id);

        log.info("Успшено удален отзыв с id: {}.", id);
    }

    @Override
    public void addLike(int reviewId, int userId) {
        String sqlQuery = "INSERT INTO review_likes (review_id, user_id, is_like) " +
                "VALUES (?, ?, true)";

        jdbcTemplate.update(sqlQuery, reviewId, userId);

        log.info("Успшено добавлен лайк " +
                "для отзыва с id: {} от пользователя {}.", reviewId, userId);
    }

    @Override
    public void deleteLike(int reviewId, int userId) {
        String sqlQuery = "DELETE FROM review_likes " +
                "WHERE review_id = ? AND user_id = ? ";

        jdbcTemplate.update(sqlQuery, reviewId, userId);

        log.info("Успшено удален лайк " +
                "для отзыва с id: {} от пользователя {}.", reviewId, userId);
    }

    @Override
    public void addDisLike(int reviewId, int userId) {
        String sqlQuery = "INSERT INTO review_likes (review_id, user_id, is_like) " +
                "VALUES (?, ?, false)";

        jdbcTemplate.update(sqlQuery, reviewId, userId);

        log.info("Успшено добавлен дизлайк " +
                "для отзыва с id: {} от пользователя {}.", reviewId, userId);
    }

    @Override
    public void deleteDisLike(int reviewId, int userId) {
        String sqlQuery = "DELETE FROM review_likes " +
                "WHERE review_id = ? AND user_id = ? ";

        jdbcTemplate.update(sqlQuery, reviewId, userId);

        log.info("Успшено удален дизлайк " +
                "для отзыва с id: {} от пользователя {}.", reviewId, userId);
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        return Review.builder()
                .id(id)
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .filmId(rs.getInt("film_id"))
                .userId(rs.getInt("user_id"))
                .useful(rs.getInt("useful")).build();
    }
}

