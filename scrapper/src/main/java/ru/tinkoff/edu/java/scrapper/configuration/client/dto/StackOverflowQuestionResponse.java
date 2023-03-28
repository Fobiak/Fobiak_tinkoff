package ru.tinkoff.edu.java.scrapper.configuration.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackOverflowQuestionResponse(
        String title,
        @JsonProperty("is_answered")
        boolean isAnswered,
        @JsonProperty("accepted_answer_id")
        int acceptedAnswerId,
        @JsonProperty("answer_count")
        int answerCount,
        @JsonProperty("creation_date")
        OffsetDateTime creationDate
) {
}