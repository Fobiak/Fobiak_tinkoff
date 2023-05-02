package ru.tinkoff.edu.java.scrapper.domain.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLink;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLinkWithInformation;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class JdbcRequestLinkRepository {
    private final JdbcTemplate template;
    private RowMapper<DataLink> rowMapperDataLink = (rs, rowNum) -> {
        try {
            Long id = rs.getLong("id");
            String url = rs.getString("url");
            return new DataLink(id, new URI(url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    };
    private RowMapper<DataLinkWithInformation> rowMapperDataLinkWithInfo = (rs, rowNum) -> {
        try {
            Long id = rs.getLong("id");
            String url = rs.getString("url");
            OffsetDateTime lastUpdate = rs.getObject("last_update", OffsetDateTime.class);
            OffsetDateTime lastEditTime = rs.getObject("last_edit_time", OffsetDateTime.class);
            Integer countAnswer = rs.getObject("count_commit_or_question", Integer.class);
            return new DataLinkWithInformation(id, new URI(url), lastUpdate, lastEditTime, countAnswer);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    };


    @Transactional
    public DataLink addLink(URI link, OffsetDateTime lastEditTime, Integer countAnswer) {
        Long idLink = linkAlreadyExist(link);
        if (idLink == null) {
            OffsetDateTime timeNow = OffsetDateTime.now();
            idLink = template.queryForObject("""
                    insert into link(url, last_update, last_edit_time, count_commit_or_question)
                    values (?, ?, ?, ?)
                    returning id""", Long.class, link.toString(), timeNow, lastEditTime, countAnswer
            );
        }
        return new DataLink(idLink, link);
    }

    @Transactional
    public DataLink removeLink(URI link) {
        Long idLink = template.queryForObject("""
                delete from link where url = ?
                RETURNING id""", Long.class, link.toString()
        );
        return new DataLink(idLink, link);
    }

    @Transactional
    public List<DataLink> findAllLinks() {
        return template.query("select id, url from link", rowMapperDataLink);
    }

    @Transactional
    public List<DataLinkWithInformation> findLinkNotUpdateLongTime(int countLink) {
        return template.query("""
                select id, url, last_update, last_edit_time, count_commit_or_question
                from link order by last_update limit ?""", rowMapperDataLinkWithInfo, countLink);
    }

    public Long linkAlreadyExist(URI url) {
        List<Long> idList = template.queryForList("""
                select id from link
                where url = ?""", Long.class, url.toString()
        );
        if (idList.size() == 0) {
            return null;
        }
        return idList.get(0);
    }
}
