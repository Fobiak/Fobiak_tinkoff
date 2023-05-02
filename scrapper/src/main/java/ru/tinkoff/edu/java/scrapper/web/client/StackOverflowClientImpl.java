package ru.tinkoff.edu.java.scrapper.web.client;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class StackOverflowClientImpl implements StackOverflowClient {
    private final String BASE_URL = "https://api.stackexchange.com";
    private final OffsetDateTime DEFAULT_DATE = OffsetDateTime.of(
            LocalDate.of(2000, 1, 1),
            LocalTime.of(1, 1, 1),
            ZoneOffset.ofHours(3));
    private final StackOverflowQuestionResponse resultDefault = new StackOverflowQuestionResponse(DEFAULT_DATE);
    private final String SITE = "site";
    private final String SITE_NAME = "stackoverflow";
    private final WebClient webClient;

    public StackOverflowClientImpl() {
        webClient = WebClient.create(BASE_URL);
    }

    public StackOverflowClientImpl(String url) {
        webClient = WebClient.create(url);
    }

    @Override
    public StackOverflowQuestionResponse fetchInfoQuestion(Long numberQuestion) {
        try {
            Mono<StackOverflowQuestionsAllResponse> response = webClient.get().uri(
                            uriBuilder -> uriBuilder.path("/questions/{number}")
                                    .queryParam(SITE, SITE_NAME)
                                    .build(numberQuestion))
                    .retrieve().bodyToMono(StackOverflowQuestionsAllResponse.class);
            StackOverflowQuestionsAllResponse questionResponses = response.block();
            return questionResponses.questionResponses()[0];
        } catch (WebClientResponseException | NullPointerException e) {
            return resultDefault;
        }
    }

    @Override
    public Mono<StackOverflowAnswersResponse> fetchAnswersRepository(Long numberQuestion) {
        return webClient.get().uri(uriBuilder ->
                        uriBuilder.path("/questions/{number}/answers")
                                .queryParam(SITE, SITE_NAME)
                                .build(numberQuestion))
                .retrieve().bodyToMono(StackOverflowAnswersResponse.class);
    }

}
