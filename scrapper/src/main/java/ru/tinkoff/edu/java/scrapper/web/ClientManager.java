package ru.tinkoff.edu.java.scrapper.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.app.enums.TypeClient;
import ru.tinkoff.app.url.UrlData;
import ru.tinkoff.app.url.UrlDataGitHub;
import ru.tinkoff.app.url.UrlDataStackOverflow;
import ru.tinkoff.edu.java.scrapper.dto.GitHubCommitsResponse;
import ru.tinkoff.edu.java.scrapper.dto.GitHubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowAnswersResponse;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.web.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.web.client.StackOverflowClient;

import java.time.OffsetDateTime;
import java.util.Objects;

//@Component
@RequiredArgsConstructor
public class ClientManager {

    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    public OffsetDateTime timeEditLinkForType(UrlData urlData) {
        return switch (urlData.getType()) {
            case GITHUB -> workWithGitHubClient((UrlDataGitHub) urlData);
            case STACKOVERFLOW -> workWithStackOverflowClient((UrlDataStackOverflow) urlData);
            default -> null;
        };
    }

    private OffsetDateTime workWithGitHubClient(UrlDataGitHub urlData) {
        Mono<GitHubRepositoryResponse> response = gitHubClient.fetchInfoRepository(urlData.userName(), urlData.repository());
        try {
            GitHubRepositoryResponse result = response.block();
            return result.timeLastUpdate();
        } catch (WebClientResponseException | NullPointerException e) {
            return OffsetDateTime.MIN;
        }
    }

    private OffsetDateTime workWithStackOverflowClient(UrlDataStackOverflow urlData) {
        StackOverflowQuestionResponse response = stackOverflowClient.fetchInfoQuestion(urlData.idQuestion());
        return response.lastEditDate();
    }

    public Integer getCountAnswer(UrlData urlData) {
        return switch (urlData.getType()) {
            case GITHUB -> getCountAnswerGitHub((UrlDataGitHub) urlData);
            case STACKOVERFLOW -> getCountAnswerStackOverflow((UrlDataStackOverflow) urlData);
            default -> null;
        };
    }

    private Integer getCountAnswerGitHub(UrlDataGitHub urlData) {
        GitHubCommitsResponse response = gitHubClient.fetchCommitsRepository(urlData.userName(), urlData.repository());
        if (response == null) {
            return 0;
        }
        return response.commitList().size();
    }

    private Integer getCountAnswerStackOverflow(UrlDataStackOverflow urlData) {
        Mono<StackOverflowAnswersResponse> response = stackOverflowClient.fetchAnswersRepository(urlData.idQuestion());
        return Objects.requireNonNull(response.block()).answers().length;
    }


    public String getInfoCountByType(TypeClient type) {
        return switch (type) {
            case GITHUB -> "New commit";
            case STACKOVERFLOW -> "New answer";
            default -> "";
        };
    }
}
