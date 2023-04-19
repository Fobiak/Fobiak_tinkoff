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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    private static final Link TEST_LINK = Link.builder()
            .id(13L)
            .url(URI.create("https://tcsbank.ru"))
            .build();
    private static final Chat TEST_CHAT = new Chat(13L);


    @Autowired
    private JdbcTemplate template;

    @Autowired
    private LinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    void when_AddingNewLink_Expect_Added() {
        linkRepository.save(TEST_LINK);
        assertEquals(1, findAll().size());
        Link link = template.queryForObject("select id, url from link where id = ?",
                new DataClassRowMapper<>(Link.class), TEST_LINK.getId());
        assertNotNull(link);
        assertAll(
                () -> assertEquals(TEST_LINK.getId(), link.getId()),
                () -> assertEquals(TEST_LINK.getUrl(), link.getUrl())
        );
    }

    @Test
    @Transactional
    @Rollback
    void when_AddingDuplicateLink_Expect_Exception() {
        linkRepository.save(TEST_LINK);
        assertEquals(1, findAll().size());
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.save(TEST_LINK));
    }

    @Test
    @Transactional
    @Rollback
    void when_RemoveExistingLink_Expect_Removed() {
        linkRepository.save(TEST_LINK);
        assertEquals(1, findAll().size());
        linkRepository.remove(TEST_LINK);
        assertEquals(0, findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void when_TableIsEmpty_Expect_FindAllReturnsZero() {
        assertEquals(0, linkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void when_TableHasOneEntry_Expect_FindAllReturnsOne() {
        linkRepository.save(TEST_LINK);
        assertEquals(1, linkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void when_AddingLinkToChat_Expect_Added() {
        createChat(TEST_CHAT.getId());
        createLink(TEST_LINK);
        linkRepository.saveToChat(TEST_CHAT.getId(), TEST_LINK.getId());
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
    void when_AddingDuplicateLinkToChat_Expect_False() {
        createChat(TEST_CHAT.getId());
        createLink(TEST_LINK);
        linkRepository.saveToChat(TEST_CHAT.getId(), TEST_LINK.getId());
        assertFalse(linkRepository.saveToChat(TEST_CHAT.getId(), TEST_LINK.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void when_RemoveExistingLinkFromChat_Expect_Removed() {
        createChat(TEST_CHAT.getId());
        createLink(TEST_LINK);
        linkRepository.saveToChat(TEST_CHAT.getId(), TEST_LINK.getId());
        assertEquals(1, findAll().size());
        assertAll(
                () -> assertTrue(linkRepository.removeFromChat(TEST_CHAT.getId(), TEST_LINK.getId())),
                () -> assertEquals(0, template.query("""
                        select id
                        from chat c
                        join subscription sub on c.id = sub.chat_id
                        where sub.link_id = ?
                        """, new DataClassRowMapper<>(Chat.class), TEST_LINK.getId()).size())
        );
    }

    @Test
    @Transactional
    @Rollback
    void when_RemoveNotExistingLinkFromChat_Expect_False() {
        assertFalse(linkRepository.removeFromChat(TEST_CHAT.getId(), TEST_LINK.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void when_ChatHasNoLinks_Expect_FindAllReturnsZero() {
        assertEquals(0, linkRepository.findAllByChat(TEST_CHAT.getId()).size());
    }

    private List<Link> findAll() {
        return template.query("select id, url from link", new DataClassRowMapper<>(Link.class));
    }

    private void createChat(long id) {
        template.update("insert into chat (id) values (?)", id);
    }

    private void createLink(Link link) {
        template.update("insert into link (id, url) values (?, ?)",
                link.getId(), link.getUrl().toString());
    }
}
