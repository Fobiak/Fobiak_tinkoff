package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dto.entity.Link;

import java.net.URI;
import java.sql.ResultSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private static final String SAVE_SQL = """
            insert into link(url)
            values (?)
            returning id
            """;
    private static final String SAVE_BY_ID_SQL = """
            insert into link(id, url)
            values (?, ?)
            """;
    private static final String REMOVE_BY_ID_SQL = """
            delete from link
            where id = ?
            """;
    private static final String FIND_ALL_SQL = """
            select id, url, updated_at
            from link
            """;
    private static final String FIND_BY_ID_SQL = """
            select id, url, updated_at
            from link
            where id = ?
            """;
    private static final String FIND_SQL = """
            select id, url, updated_at
            from link
            where url = ?
            """;
    private static final String REMOVE_SQL = """
            delete from link
            where url = ?
            """;
    private static final String ADD_LINK_TO_CHAT_SQL = """
            insert into subscription(chat_id, link_id)
            values (?, ?);
            """;
    private static final String CHECK_IF_LINK_EXISTS_IN_CHAT_SQL = """
            select exists (
                select chat_id, link_id
                from subscription
                where (chat_id, link_id) = (?, ?)
            )
            """;
    private static final String REMOVE_LINK_FROM_CHAT_SQL = """
            delete from subscription
            where chat_id = ? and link_id = ?;
            """;
    private static final String FIND_LINKS_BY_CHAT_ID_SQL = """
            select id, url, updated_at
            from link l
            join subscription sub on l.id = sub.link_id
            where sub.chat_id = ?
            """;
    public static final RowMapper<Link> LINK_ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Link.builder().id(resultSet.getLong("id")).url(URI.create(resultSet.getString("url"))).updatedAt(resultSet.getTimestamp("updated_at").toInstant()).build();


    private final JdbcTemplate template;

    public Link save(Link link) {
        if (link.getId() == null) {
            Long id = template.queryForObject(SAVE_SQL, long.class, link.getUrl().toString());
            link.setId(id);
        } else {
            template.update(SAVE_BY_ID_SQL, link.getId(), link.getUrl().toString());
        }
        return link;
    }

    public boolean removeById(long id) {
        return template.update(REMOVE_BY_ID_SQL, id) > 0;
    }

    public boolean remove(Link link) {
        return template.update(REMOVE_SQL, link.getUrl().toString()) > 0;
    }

    public Link findById(long id) {
        try {
            return template.queryForObject(FIND_BY_ID_SQL, LINK_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Link find(Link link) {
        try {
            return template.queryForObject(FIND_SQL, LINK_ROW_MAPPER, link.getUrl().toString());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Link> findAll() {
        return template.query(FIND_ALL_SQL, LINK_ROW_MAPPER);
    }

    @Override
    public boolean saveToChat(long chatId, long linkId) {
        Boolean isPresent = template.queryForObject(CHECK_IF_LINK_EXISTS_IN_CHAT_SQL, boolean.class, chatId, linkId);
        if (isPresent == null || isPresent) {
            return false;
        }

        return template.update(ADD_LINK_TO_CHAT_SQL, chatId, linkId) > 0;
    }

    @Override
    public boolean removeFromChat(long chatId, long linkId) {
        return template.update(REMOVE_LINK_FROM_CHAT_SQL, chatId, linkId) > 0;
    }

    @Override
    public List<Link> findAllByChat(long chatId) {
        return template.query(FIND_LINKS_BY_CHAT_ID_SQL, LINK_ROW_MAPPER, chatId);
    }
}
