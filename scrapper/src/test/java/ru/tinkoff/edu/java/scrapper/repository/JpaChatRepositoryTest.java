package ru.tinkoff.edu.java.scrapper.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dto.entity.Chat;
import ru.tinkoff.edu.java.scrapper.dto.entity.Link;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JpaChatRepositoryTest extends IntegrationEnvironment {
    private static final Link TEST_LINK = Link.builder()
            .id(13L)
            .url(URI.create("https://tcsbank.ru"))
            .build();
    private static final Chat TEST_CHAT = new Chat(13L);


    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void when_AddingNewChat_Expect_Added() {
        chatRepository.save(TEST_CHAT);
        Chat chat = template.queryForObject("select id from chat where id = ?",
                new DataClassRowMapper<>(Chat.class), TEST_CHAT.getId());
        assertNotNull(chat);
        assertEquals(TEST_CHAT.getId(), chat.getId());
    }

    @Test
    @Transactional
    @Rollback
    void when_AddingDuplicateChat_Expect_Exception() {
        chatRepository.save(TEST_CHAT);
        assertEquals(1, findAll().size());
        assertThrows(DataIntegrityViolationException.class, () -> chatRepository.save(TEST_CHAT));
    }

    @Test
    @Transactional
    @Rollback
    void when_RemoveExistingChat_Expect_Removed() {
        chatRepository.save(TEST_CHAT);
        assertEquals(1, findAll().size());
        chatRepository.removeById(TEST_CHAT.getId());
        assertEquals(0, findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void when_TableIsEmpty_Expect_FindAllReturnsZero() {
        assertEquals(0, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void when_TableHasOneEntry_Expect_FindAllReturnsOne() {
        chatRepository.save(TEST_CHAT);
        assertEquals(1, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void when_ThereIsNoLinkInAnyChat_Expect_FindAllReturnsZero() {
        assertEquals(0, chatRepository.findAllByLink(TEST_LINK.getId()).size());
    }

    private List<Chat> findAll() {
        return template.query("select id from chat", new DataClassRowMapper<>(Chat.class));
    }
}
