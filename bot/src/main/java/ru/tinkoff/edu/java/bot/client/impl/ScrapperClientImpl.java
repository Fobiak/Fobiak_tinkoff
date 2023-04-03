package ru.tinkoff.edu.java.bot.client.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.client.dto.AddLinkRequest;
import ru.tinkoff.edu.java.bot.client.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.client.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.client.dto.RemoveLinkRequest;

public class ScrapperClientImpl implements ScrapperClient {
    private static final String TG_URI = "/tg-chat/{id}";
    private static final String LINKS_URI = "/links/{id}";
    @Value("${scrapper.host}")
    private String url;
    private WebClient webClient;

    @PostConstruct
    private void setUpWebClient() {
        webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public ListLinksResponse getAllLinks(long id) {
        return webClient.get()
                .uri(LINKS_URI, id)
                .retrieve()
                .bodyToFlux(ListLinksResponse.class)
                .blockLast();
    }

    @Override
    public LinkResponse addLink(long id, String link) {
        return webClient.post()
                .uri(LINKS_URI, id)
                .body(BodyInserters.fromValue(new AddLinkRequest(link)))
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }

    @Override
    public LinkResponse removeLink(long id, String link) {
        return webClient.method(HttpMethod.DELETE)
                .uri(LINKS_URI, id)
                .body(BodyInserters.fromValue(new RemoveLinkRequest(link)))
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }

    @Override
    public boolean registerChat(long id) {
        return Boolean.TRUE.equals(webClient.post()
                .uri(TG_URI, id)
                .retrieve()
                .toBodilessEntity()
                .flatMap(response -> Mono.just(response
                        .getStatusCode()
                        .is2xxSuccessful()))
                .block());
    }

    @Override
    public boolean removeChat(long id) {
        return Boolean.TRUE.equals(webClient.delete()
                .uri(TG_URI, id)
                .retrieve()
                .toBodilessEntity()
                .flatMap(response -> Mono.just(response
                        .getStatusCode()
                        .is2xxSuccessful()))
                .block());
    }
}
