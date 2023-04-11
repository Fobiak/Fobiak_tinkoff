package ru.tinkoff.edu.java.bot.client;

import ru.tinkoff.edu.java.bot.dto.client.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.client.ListLinksResponse;

public interface ScrapperClient {
    boolean registerChat(long id);

    boolean removeChat(long id);

    ListLinksResponse getAllLinks(long id);

    LinkResponse addLink(long id, String link);

    LinkResponse removeLink(long id, String link);
}
