package ru.tinkoff.edu.java.scrapper.web.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

public class BotClientImpl implements BotClient {
    private final String BASE_URL = "http://localhost:8080";
    private final String PATH_LINK_UPDATER = "/updater";
    private final WebClient webClient;

    public BotClientImpl() {
        webClient = WebClient.create(BASE_URL);
    }

    public BotClientImpl(String url) {
        webClient = WebClient.create(url);
    }

    @Override
    public void updater(LinkUpdateRequest linkUpdateRequest) {
        webClient.post().uri(uriBuilder -> uriBuilder.path(PATH_LINK_UPDATER).build())
                .bodyValue(linkUpdateRequest)
                .retrieve();
    }
}
