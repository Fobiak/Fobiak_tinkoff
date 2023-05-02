package ru.tinkoff.app.parser;

import ru.tinkoff.app.enums.TypeClient;
import ru.tinkoff.app.url.UrlData;
import ru.tinkoff.app.url.UrlDataGitHub;

public final class ParserGitHubURL implements ParserURL {

    private final String TYPE_URL = "github.com";
    private final TypeClient typeClient = TypeClient.GITHUB;
    @Override
    public UrlData parseUrl(String url) {
        String[] args = url.split("/");
        if (args.length > 4 && args[2].equals(TYPE_URL)) {
            return new UrlDataGitHub(typeClient, args[3], args[4]);
        }
        return null;
    }
}
