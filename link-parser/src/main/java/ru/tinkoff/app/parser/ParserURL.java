package ru.tinkoff.app.parser;

import ru.tinkoff.app.url.UrlData;


public sealed interface ParserURL permits ParserGitHubURL, ParserStackOverflowURL {
    UrlData parseUrl(String url);
}
