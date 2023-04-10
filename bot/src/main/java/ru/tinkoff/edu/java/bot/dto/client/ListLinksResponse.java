package ru.tinkoff.edu.java.bot.dto.client;

import java.util.List;

public record ListLinksResponse(
        List<LinkResponse> links,
        int size
) {
}
