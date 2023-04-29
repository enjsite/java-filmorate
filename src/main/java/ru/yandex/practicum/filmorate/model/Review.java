package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Review {

    @JsonProperty("reviewId")
    private int id;

    @NotBlank(message = "Отзыв не может быть без описания.")
    @NotNull(message = "Отзыв не может быть без описания.")
    private String content;

    @NotNull(message = "Не указан тип отзыва.")
    @JsonProperty(value = "isPositive")
    private Boolean isPositive;

    @NotNull(message = "Не указан автор отзыва.")
    private Integer userId;

    @NotNull(message = "Не указан фильм, по которому отправлен отзыва.")
    private Integer filmId;
    private int useful;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("content", content);
        values.put("is_positive", isPositive);
        values.put("user_id", userId);
        values.put("film_id", filmId);
        return values;
    }
}
