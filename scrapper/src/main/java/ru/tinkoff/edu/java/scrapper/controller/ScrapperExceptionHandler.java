package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;

import java.util.Arrays;
import java.util.NoSuchElementException;

@RestControllerAdvice(
        basePackageClasses = {TgChatController.class, LinkController.class},
        basePackages = "ru.tinkoff.edu.java.scrapper.controller"
)
public class ScrapperExceptionHandler {
    @ExceptionHandler(ErrorResponseException.class)
    public ApiErrorResponse handleWithErrorResponseException(ErrorResponseException e) {
        return new ApiErrorResponse(
                e.getMessage(),
                e.getStatusCode().toString(),
                e.getDetailMessageCode(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiErrorResponse handleWithConstraintViolationException(ConstraintViolationException e) {
        return new ApiErrorResponse(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.toString(),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ApiErrorResponse handleWithNoSuchElementException(NoSuchElementException e) {
        return new ApiErrorResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND.toString(),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList());
    }
}
