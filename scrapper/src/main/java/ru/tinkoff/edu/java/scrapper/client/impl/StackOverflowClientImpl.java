package ru.tinkoff.edu.java.scrapper.client.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.dto.StackOverflowQuestionResponse;

public class StackOverflowClientImpl implements StackOverflowClient {
    private static final String URI = "/questions/{id}";
    private static final String BASE_URL = "https://api.stackexchange.com/2.3";
    private final String url;
    private WebClient webClient;


    public StackOverflowClientImpl(String url) {
        this.url = url;
    }

    public StackOverflowClientImpl() {
        this(BASE_URL);
    }

    @PostConstruct
    private void setUpWebClient() {
        webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mono<StackOverflowQuestionResponse> getQuestionById(int id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .queryParam("site", "stackoverflow")
                        .build(id))
                .retrieve()
                .bodyToMono(StackOverflowQuestionResponse.class);
    }
}
