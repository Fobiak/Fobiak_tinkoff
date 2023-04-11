package ru.tinkoff.edu.java.bot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record LinkUpdateRequest(
        @PositiveOrZero
        long id,
        @NotBlank
        String url,
        @NotBlank
        String description,
        @NotNull
        List<Long> tgChatIds
) {
}