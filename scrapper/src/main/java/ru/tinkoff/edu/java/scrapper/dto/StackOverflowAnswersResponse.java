package ru.tinkoff.edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowAnswersResponse(@JsonProperty("items") StackOverflowAnswer[] answers) {
}
