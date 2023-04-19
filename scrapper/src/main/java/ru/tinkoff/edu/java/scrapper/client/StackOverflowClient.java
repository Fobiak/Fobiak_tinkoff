package ru.tinkoff.edu.java.scrapper.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.client.StackOverflowQuestionResponse;

public interface StackOverflowClient {
    Mono<StackOverflowQuestionResponse> getQuestionById(int id);
}