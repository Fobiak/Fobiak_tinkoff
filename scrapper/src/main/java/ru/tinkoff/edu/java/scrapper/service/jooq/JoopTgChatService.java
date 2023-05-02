package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.domain.jooq.JooqRequestClientRepository;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUserWithInfo;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@RequiredArgsConstructor
public class JoopTgChatService implements TgChatService {

    private final JooqRequestClientRepository jooqRequestClientRepository;

    @Override
    public void register(long tgChatId, String userName) {
        jooqRequestClientRepository.addUser(tgChatId, userName);
    }

    @Override
    public void unregister(long tgChatId) {
        jooqRequestClientRepository.removeUser(tgChatId);
    }

    @Override
    public void updateStateUser(long tgChatId, String userState) {
        jooqRequestClientRepository.updateStateUser(tgChatId, userState);
    }

    @Override
    public DataUserWithInfo getUser(long tgChatId) {
        return jooqRequestClientRepository.getUser(tgChatId);
    }
}
