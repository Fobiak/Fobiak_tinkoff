package ru.tinkoff.edu.java.bot.dto;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public record ApiErrorResponse(
        String description,
        String code,
        Class<? extends Exception> exceptionName,
        String exceptionMessage,
        List<String> stacktrace
) {
    public ApiErrorResponse(Exception e, HttpStatus code, String description) {
        this(description,
                code.toString(),
                e.getClass(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList());
    }
}
