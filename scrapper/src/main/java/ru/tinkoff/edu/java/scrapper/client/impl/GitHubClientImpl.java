package ru.tinkoff.edu.java.scrapper.client.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.dto.GitHubRepositoryResponse;

public class GitHubClientImpl implements GitHubClient {
    private static final String URI = "/repos/{owner}/{repo}";
    private static final String BASE_URL = "https://api.github.com";
    private final String url;
    private WebClient webClient;

    public GitHubClientImpl(String url) {
        this.url = url;
    }

    public GitHubClientImpl() {
        this(BASE_URL);
    }

    @PostConstruct
    private void setUpWebClient() {
        webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mono<GitHubRepositoryResponse> getRepository(String owner, String repo) {
        return webClient.get()
                .uri(URI, owner, repo)
                .retrieve()
                .bodyToMono(GitHubRepositoryResponse.class);
    }
}