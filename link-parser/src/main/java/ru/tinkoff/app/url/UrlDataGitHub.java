package ru.tinkoff.app.url;

import ru.tinkoff.app.enums.TypeClient;

public record UrlDataGitHub(TypeClient typeUrl, String userName, String repository) implements UrlData {
    @Override
    public TypeClient getType() {
        return typeUrl;
    }
}
