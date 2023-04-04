package ru.tinkoff.edu.java.bot.dto;

import org.springframework.http.HttpStatus;

public record LinkUpdateResponse(
        String message,
        HttpStatus statusCode
) {
}
