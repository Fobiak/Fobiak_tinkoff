package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements TgChatService {
    private final ChatRepository chatRepository;

    @Override
    public void register(long tgChatId) {
        chatRepository.save(new Chat(tgChatId));
    }

    @Override
    public void unregister(long tgChatId) {
        chatRepository.removeById(tgChatId);
    }
}
