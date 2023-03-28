package ru.tinkoff.edu.java.dto;

public sealed interface UrlData permits GitHubData, StackOverflowData {
}
