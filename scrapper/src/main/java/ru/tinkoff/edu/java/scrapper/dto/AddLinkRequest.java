package ru.tinkoff.edu.java.scrapper.dto;

import jakarta.validation.constraints.NotBlank;

public record AddLinkRequest(
        @NotBlank
        String link
) {
}
