package ru.tinkoff.edu.java.scrapper.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dto.entity.Chat;
import ru.tinkoff.edu.java.scrapper.dto.entity.Link;
import ru.tinkoff.edu.java.scrapper.dto.entity.Subscription;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcSubscriptionRepositoryTest extends IntegrationEnvironment {
    private static final Chat TEST_CHAT = new Chat(13L);
    private static final Link TEST_LINK = Link.builder()
            .id(1L)
            .url(URI.create("https://tcsbank.ru"))
            .build();


    @Autowired
    private JdbcTemplate template;

    @Autowired
    private JdbcSubscriptionRepository subscriptionRepository;

    @Test
    @Transactional
    @Rollback
    void when_AddingLinkToChat_Expect_Added() {
        createChat(TEST_CHAT.getId());
        subscriptionRepository.saveLinkToChat(TEST_CHAT.getId(), TEST_LINK);
        List<Link> links = template.query("""
                select id, url, updated_at
                from link l
                join subscription sub on l.id = sub.link_id
                where sub.chat_id = ?
                """, new DataClassRowMapper<>(Link.class), TEST_CHAT.getId());
        assertEquals(1, links.size());
    }

    @Test
    @Transactional
    @Rollback
    void when_AddingDuplicateLinkToChat_Expect_Exception() {
        createChat(TEST_CHAT.getId());
        subscriptionRepository.saveLinkToChat(TEST_CHAT.getId(), TEST_LINK);
        assertThrows(DataIntegrityViolationException.class,
                () -> subscriptionRepository.saveLinkToChat(TEST_CHAT.getId(), TEST_LINK));
    }

    @Test
    @Transactional
    @Rollback
    void when_RemoveExistingLinkFromChat_Expect_Removed() {
        createChat(TEST_CHAT.getId());
        subscriptionRepository.saveLinkToChat(TEST_CHAT.getId(), TEST_LINK);
        int beforeRemovingCount = findAll().size();
        subscriptionRepository.removeLinkFromChat(TEST_CHAT.getId(), TEST_LINK);
        int afterRemovingCount = findAll().size();
        assertEquals(beforeRemovingCount, afterRemovingCount + 1);
    }

    @Test
    @Transactional
    @Rollback
    void when_RemoveNotExistingLinkFromChat_Expect_Exception() {
        assertThrows(EmptyResultDataAccessException.class,
                () -> subscriptionRepository.removeLinkFromChat(TEST_CHAT.getId(), TEST_LINK));
    }

    @Test
    @Transactional
    @Rollback
    void when_ChatHasNoLinks_Expect_FindAllReturnsZero() {
        assertEquals(0, subscriptionRepository.findAllLinksByChat(TEST_CHAT.getId()).size());
    }

    @Test
    @Transactional
    @Rollback
    void when_ThereIsNoLinkInAnyChat_Expect_FindAllReturnsZero() {
        assertThrows(EmptyResultDataAccessException.class,
                () -> subscriptionRepository.findAllChatsByLink(TEST_LINK).size());
    }

    private List<Subscription> findAll() {
        return template.query("select chat_id, link_id from subscription",
                new DataClassRowMapper<>(Subscription.class));
    }

    private void createChat(long id) {
        template.update("insert into chat (id) values (?)", id);
    }
}
