package ru.tinkoff.edu.java.scrapper.web.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.GitHubCommitsResponse;
import ru.tinkoff.edu.java.scrapper.dto.GitHubRepositoryResponse;

public interface GitHubClient {
    Mono<GitHubRepositoryResponse> fetchInfoRepository(String userName, String repo);

    GitHubCommitsResponse fetchCommitsRepository(String userName, String repo);
}
