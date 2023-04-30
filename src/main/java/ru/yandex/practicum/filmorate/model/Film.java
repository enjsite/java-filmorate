package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film implements Comparable<Film> {

    private int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(min = 1, max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    private LocalDate releaseDate;
    private int duration;

    private List<Integer> likes = new ArrayList<>();

    private List<Genre> genres = new ArrayList<>();

    private Set<Director> directors = new HashSet<>();

    private Rating mpa;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, Rating mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Rating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa", mpa.getId());
        return values;
    }

    @Override
    public int compareTo(Film anotherFilm) {
        return Integer.compare(this.id, anotherFilm.getId());
    }
}
