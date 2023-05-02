package ru.tinkoff.edu.java.scrapper.domain.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.jooq.codegen.tables.Link;
import ru.tinkoff.edu.java.scrapper.domain.jooq.codegen.tables.UserLinks;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLink;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLinkWithInformation;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JooqRequestLinkRepository {

    private final DSLContext dslContext;

    @Transactional
    public DataLink addLink(URI link, OffsetDateTime lastEditTime, Integer countAnswer) {
        Long idLink = linkAlreadyExist(link);
        if (idLink == null) {
            idLink = Long.valueOf(dslContext.insertInto(Link.LINK)
                    .set(Link.LINK.URL, link.toString())
                    .set(Link.LINK.LAST_EDIT_TIME, lastEditTime.toLocalDateTime())
                    .set(Link.LINK.LAST_UPDATE, OffsetDateTime.now().toLocalDateTime())
                    .set(Link.LINK.COUNT_COMMIT_OR_QUESTION, countAnswer)
                    .returning(Link.LINK.ID)
                    .fetchOptional()
                    .orElseThrow(() -> new DataAccessException("Error inserting entity"))
                    .get(Link.LINK.ID));
        }
        return new DataLink(idLink, link);
    }

    @Transactional
    public DataLink removeLink(URI link) {
        Long idLink = Long.valueOf(dslContext.delete(Link.LINK)
                .where(Link.LINK.URL.eq(link.toString()))
                .returning(Link.LINK.ID)
                .fetchOne()
                .getId());
        return new DataLink(idLink, link);
    }

    @Transactional
    public List<DataLink> findAllLinks() {
        List<DataLink> linkRecords = dslContext.selectFrom(Link.LINK)
                .fetch().stream().map(x -> {
                    try {
                        return new DataLink(Long.valueOf(x.getId()), new URI(x.getUrl()));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
        return linkRecords;
    }

    @Transactional
    public List<DataLink> findLinksOnUser(long tgChatId) {
        Set<Long> setIdLink = dslContext.select(UserLinks.USER_LINKS)
                .where(UserLinks.USER_LINKS.LINKS_ID.eq(tgChatId))
                .fetch(Link.LINK.ID).stream().map(x -> Long.valueOf(x)).collect(Collectors.toSet());
        List<DataLink> linkRecords = dslContext.selectFrom(Link.LINK)
                .where(Link.LINK.ID.in(setIdLink))
                .fetch().stream().map(x -> {
                    try {
                        return new DataLink(Long.valueOf(x.getId()), new URI(x.getUrl()));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
        return linkRecords;
    }

    @Transactional
    public List<DataLinkWithInformation> findLinkNotUpdateLongTime(int countLink) {
        return dslContext.selectFrom(Link.LINK)
                .fetch().stream().map(x -> {
                    try {
                        return new DataLinkWithInformation(Long.valueOf(x.getId()), new URI(x.getUrl()),
                                x.getLastUpdate().atOffset(ZoneOffset.ofHours(3)), x.getLastEditTime().atOffset(ZoneOffset.ofHours(3)), x.getCountCommitOrQuestion());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    public Long linkAlreadyExist(URI url) {
        Long idLink = dslContext.select(Link.LINK.ID)
                .from(Link.LINK)
                .where(Link.LINK.URL.eq(url.toString()))
                .fetchOne(0, Long.class);
        return idLink;
    }
}
