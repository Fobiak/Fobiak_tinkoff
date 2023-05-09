package ru.tinkoff.app.parser;

import ru.tinkoff.app.enums.TypeClient;
import ru.tinkoff.app.url.UrlData;
import ru.tinkoff.app.url.UrlDataStackOverflow;

public final class ParserStackOverflowURL implements ParserURL {

    private final String QUESTIONS = "questions";
    private final String TYPE_URL = "stackoverflow.com";

    private final TypeClient typeClient = TypeClient.STACKOVERFLOW;

    @Override
    public UrlData parseUrl(String url) {
        String[] args = url.split("/");
        if (args.length > 4 && args[2].equals(TYPE_URL) && args[3].equals(QUESTIONS)) {
            try {
                Long id = Long.parseLong(args[4]);
                return new UrlDataStackOverflow(typeClient, id);
            } catch (NumberFormatException e) {
                // Bad url
                return null;
            }
        }
        return null;
    }
}
