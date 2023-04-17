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
import ru.tinkoff.edu.java.scrapper.dto.entity.Link;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    private static final Link TEST_LINK = Link.builder()
            .id(13L)
            .url(URI.create("https://tcsbank.ru"))
            .build();


    @Autowired
    private JdbcTemplate template;

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    void when_AddingNewLink_Expect_Added() {
        linkRepository.save(TEST_LINK);
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
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.save(TEST_LINK));
    }

    @Test
    @Transactional
    @Rollback
    void when_RemoveExistingLink_Expect_Removed() {
        linkRepository.save(TEST_LINK);
        int beforeRemovingCount = findAll().size();
        linkRepository.remove(TEST_LINK);
        int afterRemovingCount = findAll().size();
        assertEquals(beforeRemovingCount, afterRemovingCount + 1);
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

    private List<Link> findAll() {
        return template.query("select id, url from link", new DataClassRowMapper<>(Link.class));
    }
}
