package ru.tinkoff.app.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.app.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.JdbcRequestClientRepository;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.JdbcRequestLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.JdbcRequestUserLinksRepository;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;


@SpringBootTest
public class JdbcRequestTableRepository extends IntegrationEnvironment {
    protected final DataSource dataSource;
    protected final URI TEST_URL = new URI("https://github.com/person/rep/");
    protected final String USER_NAME = "Alex";
    protected final Long CHAT_ID = 13424L;
    protected final JdbcRequestClientRepository userTable;
    protected final JdbcRequestLinkRepository linkTable;
    protected final JdbcRequestUserLinksRepository userLinksTable;


    public JdbcRequestTableRepository() throws URISyntaxException {
        dataSource = initDatasource();
        JdbcTemplate template = new JdbcTemplate(dataSource);
        this.userTable = new JdbcRequestClientRepository(template);
        this.linkTable = new JdbcRequestLinkRepository(template);
        this.userLinksTable = new JdbcRequestUserLinksRepository(template);
    }

    private DataSource initDatasource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(POSTGRES_CONTAINER.getJdbcUrl());
        hikariConfig.setUsername(POSTGRES_CONTAINER.getUsername());
        hikariConfig.setPassword(POSTGRES_CONTAINER.getPassword());

        return new HikariDataSource(hikariConfig);
    }
}
