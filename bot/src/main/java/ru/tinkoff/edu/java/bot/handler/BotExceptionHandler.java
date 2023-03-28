package ru.tinkoff.edu.java.bot.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.bot.controller.UpdateController;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;

import java.util.Arrays;

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