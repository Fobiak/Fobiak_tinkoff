package ru.tinkoff.edu.java.bot.dto.client;

import jakarta.validation.constraints.NotBlank;

public record RemoveLinkRequest(
        @NotBlank
        String link
) {
}
