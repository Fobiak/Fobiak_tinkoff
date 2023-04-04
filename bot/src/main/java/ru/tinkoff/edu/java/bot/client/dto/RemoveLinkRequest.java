package ru.tinkoff.edu.java.bot.client.dto;

import jakarta.validation.constraints.NotBlank;

public record RemoveLinkRequest(
        @NotBlank
        String link
) {
}
