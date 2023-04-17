package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dto.entity.Link;

import java.net.URI;
import java.sql.ResultSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository {
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
    public static final RowMapper<Link> LINK_ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Link.builder()
            .id(resultSet.getLong("id"))
            .url(URI.create(resultSet.getString("url")))
            .updatedAt(resultSet.getTimestamp("updated_at").toInstant())
            .build();


    private final JdbcTemplate template;

    public void save(Link link) {
        if (link.getId() > 0) {
            template.update(SAVE_BY_ID_SQL, link.getId(), link.getUrl().toString());
        } else {
            long id = template.queryForObject(SAVE_SQL, Long.class, link.getUrl().toString());
            link.setId(id);
        }
    }

    public int removeById(long id) {
        return template.update(REMOVE_BY_ID_SQL, id);
    }

    public int remove(Link link) {
        return template.update(REMOVE_SQL, link.getUrl().toString());
    }

    public Link findById(long id) {
        return template.queryForObject(FIND_BY_ID_SQL, LINK_ROW_MAPPER, id);
    }

    public Link find(Link link) {
        return template.queryForObject(FIND_SQL, LINK_ROW_MAPPER, link.getUrl().toString());
    }

    public List<Link> findAll() {
        return template.query(FIND_ALL_SQL, LINK_ROW_MAPPER);
    }
}
