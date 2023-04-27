package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@SuppressWarnings("checkstyle:Regexp")
@Slf4j
@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) {
        log.error("Ошибка валидации: " + e.getMessage());
        return new ErrorResponse(
                "Ошибка валидации", e.getMessage()
        );
    }

    @ExceptionHandler({NullPointerException.class,
            EmptyResultDataAccessException.class,
            DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final RuntimeException e) {
        log.error("Объект не найден: " + e.getMessage());
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

    @SuppressWarnings("checkstyle:MethodName")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String NotFoundException(NotFoundException exception) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(exception.getMessage());
    }




    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final EmptyResultDataAccessException e) {
        log.error("Объект не найден: " + e.getMessage());
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }*/

   /* @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final DataIntegrityViolationException e) {
        log.error("Объект не найден: " + e.getMessage());
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }*/

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final Exception e) {
        log.error("Возникло исключение: " + e.getMessage());
        return new ErrorResponse(
                "Возникло исключение", e.getMessage()
        );
    }
}
