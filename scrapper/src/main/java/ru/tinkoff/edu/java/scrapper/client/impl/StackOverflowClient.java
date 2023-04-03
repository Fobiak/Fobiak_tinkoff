package ru.tinkoff.edu.java.scrapper.client.impl;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.dto.StackOverflowQuestionResponse;

public interface StackOverflowClient {
    Mono<StackOverflowQuestionResponse> getQuestionById(int id);
}