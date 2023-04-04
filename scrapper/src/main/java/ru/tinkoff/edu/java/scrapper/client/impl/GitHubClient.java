package ru.tinkoff.edu.java.scrapper.client.impl;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.dto.GitHubRepositoryResponse;

public interface GitHubClient {
    Mono<GitHubRepositoryResponse> getRepository(String owner, String repo);
}
