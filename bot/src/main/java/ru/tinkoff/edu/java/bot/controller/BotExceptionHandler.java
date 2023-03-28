package ru.tinkoff.edu.java.bot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;


@RestControllerAdvice(
        basePackageClasses = UpdateController.class,
        basePackages = "ru.tinkoff.edu.java.bot.controller"
)
public class BotExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiErrorResponse handleWithIllegalArgumentException(IllegalArgumentException e) {
        return new ApiErrorResponse(e, HttpStatus.BAD_REQUEST, "Некорректные параметры запроса");
    }
}