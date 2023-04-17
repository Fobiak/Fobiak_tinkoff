package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dto.entity.Chat;
import ru.tinkoff.edu.java.scrapper.dto.entity.Link;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository.CHAT_ROW_MAPPER;
import static ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository.LINK_ROW_MAPPER;

@Repository
@RequiredArgsConstructor
public class JdbcSubscriptionRepository {
    private static final String ADD_LINK_TO_CHAT_SQL = """
            insert into subscription(chat_id, link_id)
            values (?, ?);
            """;
    private static final String REMOVE_LINK_FROM_CHAT_SQL = """
            delete from subscription
            where chat_id = ? and link_id = ?;
            """;
    private static final String FIND_CHATS_BY_LINK_SQL = """
            select id
            from chat c
            join subscription sub on c.id = sub.chat_id
            where sub.link_id = ?
            """;
    private static final String FIND_LINKS_BY_CHAT_ID_SQL = """
            select id, url, updated_at
            from link l
            join subscription sub on l.id = sub.link_id
            where sub.chat_id = ?
            """;


    private final JdbcChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcTemplate template;

    public void saveLinkToChat(long chatId, Link link) {
        Chat chat = chatRepository.findById(chatId);
        linkRepository.save(link);
        template.update(ADD_LINK_TO_CHAT_SQL, chat.getId(), link.getId());
    }

    public void removeLinkFromChat(long chatId, Link link) {
        Chat chat = chatRepository.findById(chatId);
        link = linkRepository.find(link);
        template.update(REMOVE_LINK_FROM_CHAT_SQL, chat.getId(), link.getId());
    }

    public List<Chat> findAllChatsByLink(Link link) {
        Link realLink = linkRepository.find(link);
        return template.query(FIND_CHATS_BY_LINK_SQL, CHAT_ROW_MAPPER, realLink.getId());
    }

    public List<Link> findAllLinksByChat(long chatId) {
        return template.query(FIND_LINKS_BY_CHAT_ID_SQL, LINK_ROW_MAPPER, chatId);
    }
}
