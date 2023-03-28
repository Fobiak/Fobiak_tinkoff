package ru.tinkoff.edu.java.scrapper.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.scrapper.controller.LinkController;
import ru.tinkoff.edu.java.scrapper.controller.TgChatController;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;

import java.util.NoSuchElementException;

@RestControllerAdvice(
        basePackageClasses = {TgChatController.class, LinkController.class},
        basePackages = "ru.tinkoff.edu.java.scrapper.controller"
)
public class ScrapperExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiErrorResponse handleWithIllegalArgumentException(IllegalArgumentException e) {
        return new ApiErrorResponse(e, HttpStatus.BAD_REQUEST, "Некорректные параметры запроса");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ApiErrorResponse handleWithNoSuchElementException(NoSuchElementException e) {
        return new ApiErrorResponse(e, HttpStatus.NOT_FOUND, "Ссылка не найдена");
    }
}
