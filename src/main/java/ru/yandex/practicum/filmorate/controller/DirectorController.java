package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping
    public Director create(@Valid @RequestBody Director director) throws ValidationException {
        log.info("Получен запрос на создание нового режиссёра");
        return directorService.create(director);
    }

    @GetMapping("/{id}")
    public Director get(@PathVariable Integer id) throws ValidationException {
        log.info("Получить режиссёра по id " + id);
        return directorService.get(id);
    }


    @PutMapping
    public Director update(@Valid @RequestBody Director director) throws ValidationException {
        log.info("Обновить режиссёра по id " + director.getId());
        return directorService.update(director);
    }

    @GetMapping
    public List<Director> getAll() {
        return directorService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        directorService.delete(id);
    }
}
