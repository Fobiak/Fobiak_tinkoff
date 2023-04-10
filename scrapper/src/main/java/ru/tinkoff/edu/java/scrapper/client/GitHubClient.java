package ru.tinkoff.edu.java.scrapper.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.client.GitHubRepositoryResponse;

public interface GitHubClient {
    Mono<GitHubRepositoryResponse> getRepository(String owner, String repo);
}
