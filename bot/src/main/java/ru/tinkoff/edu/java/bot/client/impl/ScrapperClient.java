package ru.tinkoff.edu.java.bot.client.impl;

import ru.tinkoff.edu.java.bot.client.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.client.dto.ListLinksResponse;

public interface ScrapperClient {
    boolean registerChat(long id);

    boolean removeChat(long id);

    ListLinksResponse getAllLinks(long id);

    LinkResponse addLink(long id, String link);

    LinkResponse removeLink(long id, String link);
}
