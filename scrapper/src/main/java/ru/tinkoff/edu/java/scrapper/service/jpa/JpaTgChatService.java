package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.jpa.JpaRequestClientRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.ClientEntity;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUserWithInfo;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {

    private final JpaRequestClientRepository jpaRequestClientRepository;
    private final static String DEFAULT_STATE = "NONE";

    @Transactional
    @Override
    public void register(long tgChatId, String userName) {
        ClientEntity client = new ClientEntity();
        client.setId(tgChatId);
        client.setUserName(userName);
        client.setUserState(DEFAULT_STATE);
        jpaRequestClientRepository.save(client);
    }

    @Transactional
    @Override
    public void unregister(long tgChatId) {
        jpaRequestClientRepository.deleteClientEntitiesById(tgChatId);
    }

    @Transactional
    @Override
    public void updateStateUser(long tgChatId, String userState) {
        ClientEntity client = jpaRequestClientRepository.getReferenceById(tgChatId);
        client.setUserState(userState);
        jpaRequestClientRepository.save(client);
    }

    @Transactional
    @Override
    public DataUserWithInfo getUser(long tgChatId) {
        ClientEntity client = jpaRequestClientRepository.getReferenceById(tgChatId);
        return new DataUserWithInfo(client.getId(), client.getUserName(), client.getUserState());
    }
}
