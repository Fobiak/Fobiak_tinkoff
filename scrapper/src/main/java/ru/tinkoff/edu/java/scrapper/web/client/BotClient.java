package ru.tinkoff.edu.java.scrapper.web.client;

import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

public interface BotClient {
    void updater(LinkUpdateRequest linkUpdaterRequest);
}
