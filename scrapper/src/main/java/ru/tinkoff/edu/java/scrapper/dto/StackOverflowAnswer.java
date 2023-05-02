package ru.tinkoff.edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowAnswer(@JsonProperty("answer_id") Long id) {
}
