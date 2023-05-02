package ru.tinkoff.app.url;

import ru.tinkoff.app.enums.TypeClient;

public record UrlDataStackOverflow(TypeClient typeUrl, Long idQuestion) implements UrlData {
    @Override
    public TypeClient getType() {
        return typeUrl;
    }
}
