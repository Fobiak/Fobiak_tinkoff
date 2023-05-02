package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.db.DataUserWithInfo;

public interface TgChatService {
    void register(long tgChatId, String userName);
    void unregister(long tgChatId);

    void updateStateUser(long tgChatId, String userState);

    DataUserWithInfo getUser(long tgChatId);
}
