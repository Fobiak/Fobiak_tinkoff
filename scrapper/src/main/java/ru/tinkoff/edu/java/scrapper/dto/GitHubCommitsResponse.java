package ru.tinkoff.edu.java.scrapper.dto;

import java.util.List;

public record GitHubCommitsResponse(List<GitHunCommit> commitList) {
}
