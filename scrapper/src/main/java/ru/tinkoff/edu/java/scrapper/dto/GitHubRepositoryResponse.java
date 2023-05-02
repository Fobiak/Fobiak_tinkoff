package ru.tinkoff.edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GitHubRepositoryResponse(@JsonProperty("name") String name,
                                       @JsonProperty("updated_at") OffsetDateTime timeLastUpdate) { }