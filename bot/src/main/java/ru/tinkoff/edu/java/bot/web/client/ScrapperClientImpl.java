package ru.tinkoff.edu.java.bot.web.client;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.*;
import ru.tinkoff.edu.java.bot.enums.StateUser;

public class ScrapperClientImpl implements ScrapperClient {
    private final String BASE_URL = "http://localhost:8077";

    private final String PATH_LINK_REQUEST = "/links";
    private final String PARAM_ID_LINK_REQUEST = "Tg-Chat-Id";

    private final String PATH_ID_REQUEST_CHATS = "/tg-chat";
    private final String PATH_ID_REQUEST_UPDATE_CHATS = "/tg-chat/update";
    private final WebClient webClient;

    public ScrapperClientImpl() {
        webClient = WebClient.create(BASE_URL);
    }

    public ScrapperClientImpl(String url) {
        webClient = WebClient.create(url);
    }

    @Override
    public Mono<AddLinkResponse> addTrackedLink(AddLinkRequest request) {
        return webClient.post()
                .uri(
                        uriBuilder -> uriBuilder.
                                path(PATH_LINK_REQUEST)
                                .queryParam(PARAM_ID_LINK_REQUEST, request.id())
                                .build())
                .bodyValue(request.url())
                .retrieve()
                .bodyToMono(AddLinkResponse.class);
    }

    @Override
    public Mono<DeleteLinkResponse> deleteTrackedLink(DeleteLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
                .uri(
                        uriBuilder -> uriBuilder.path(PATH_LINK_REQUEST)
                                .queryParam(PARAM_ID_LINK_REQUEST, request.id())
                                .build())
                .bodyValue(request.url())
                .retrieve()
                .bodyToMono(DeleteLinkResponse.class);
    }

    @Override
    public Mono<ListLinkResponse> listTrackedLink(ListLinkRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder.path(PATH_LINK_REQUEST)
                                .queryParam(PARAM_ID_LINK_REQUEST, request.id())
                                .build())
                .retrieve()
                .bodyToMono(ListLinkResponse.class);
    }

    @Override
    public void registerChat(Long id, String userName) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path(PATH_ID_REQUEST_CHATS + "/{id}")
                        .build(id))
                .bodyValue(userName)
                .retrieve()
                .bodyToMono(void.class)
                .block();
    }

    @Override
    public void deleteChat(Long id) {
        webClient.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.path(PATH_ID_REQUEST_CHATS + "/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(void.class)
                .block();
    }

    @Override
    public void updateUser(Long id, StateUser stateUser) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path(PATH_ID_REQUEST_UPDATE_CHATS + "/{id}")
                                .build(id))
                .bodyValue(stateUser.toString())
                .retrieve()
                .bodyToMono(void.class)
                .block();
    }

    @Override
    public Mono<UserGetResponse> getUser(Long id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(PATH_ID_REQUEST_CHATS + "/{id}")
                        .build(id))
                .retrieve().bodyToMono(UserGetResponse.class);
    }

}
