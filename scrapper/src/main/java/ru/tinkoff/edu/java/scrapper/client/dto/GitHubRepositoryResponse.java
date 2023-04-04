package ru.tinkoff.edu.java.scrapper.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GitHubRepositoryResponse(
        int id,
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("created_at")
        OffsetDateTime createdAt,
        @JsonProperty("updated_at")
        OffsetDateTime updatedAt
) {
}
