package ru.tinkoff.edu.java.scrapper.web.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowAnswersResponse;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowQuestionResponse;

public interface StackOverflowClient {
    StackOverflowQuestionResponse fetchInfoQuestion(Long numberQuestion);

    Mono<StackOverflowAnswersResponse> fetchAnswersRepository(Long numberQuestion);
}

