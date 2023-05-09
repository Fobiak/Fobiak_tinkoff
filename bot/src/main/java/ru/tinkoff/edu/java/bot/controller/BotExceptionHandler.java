package ru.tinkoff.edu.java.bot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(
        basePackageClasses = UpdatesController.class
)
public class BotExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleNullPointerException(Exception ex) {
        logger.error("Exception", ex);
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }
}
