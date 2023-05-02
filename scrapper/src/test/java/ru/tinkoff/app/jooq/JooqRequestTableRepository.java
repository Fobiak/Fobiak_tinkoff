package ru.tinkoff.app.jooq;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.app.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.domain.jooq.JooqRequestClientRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.JooqRequestLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.JooqRequestUserLinksRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@SpringBootTest
public class JooqRequestTableRepository extends IntegrationEnvironment {
    protected final URI TEST_URL = new URI("https://github.com/person/rep/");
    protected final String USER_NAME = "Alex";
    protected final Long CHAT_ID = 13424L;
    protected final JooqRequestClientRepository userTable;
    protected final JooqRequestLinkRepository linkTable;
    protected final JooqRequestUserLinksRepository userLinksTable;


    public JooqRequestTableRepository() throws URISyntaxException, SQLException {
        Connection connection = DriverManager.getConnection(POSTGRES_CONTAINER.getJdbcUrl(), POSTGRES_CONTAINER.getUsername(), POSTGRES_CONTAINER.getPassword());
        DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
        this.userTable = new JooqRequestClientRepository(context);
        this.linkTable = new JooqRequestLinkRepository(context);
        this.userLinksTable = new JooqRequestUserLinksRepository(context);
    }
}
