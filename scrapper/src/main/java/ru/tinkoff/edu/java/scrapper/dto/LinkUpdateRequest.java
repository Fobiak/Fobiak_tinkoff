package ru.tinkoff.edu.java.scrapper.dto;

import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(Long id, URI url, String description, List<Long> tgChatIds) {
}
