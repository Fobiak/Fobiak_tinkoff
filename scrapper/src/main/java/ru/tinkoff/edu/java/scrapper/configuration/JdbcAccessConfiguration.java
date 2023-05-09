package ru.tinkoff.edu.java.scrapper.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.JdbcRequestClientRepository;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.JdbcRequestLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.JdbcRequestUserLinksRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgChatService;
import ru.tinkoff.edu.java.scrapper.web.ClientManager;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/scrapper";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    @Bean
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(DATABASE_URL);
        hikariConfig.setUsername(USERNAME);
        hikariConfig.setPassword(PASSWORD);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public JdbcRequestClientRepository getJdbcRequestClientRepository(JdbcTemplate template) {
        return new JdbcRequestClientRepository(template);
    }

    @Bean
    public JdbcRequestLinkRepository getJdbcRequestLinkRepository(JdbcTemplate template) {
        return new JdbcRequestLinkRepository(template);
    }

    @Bean
    public JdbcRequestUserLinksRepository getJdbcRequestUserLinksRepository(JdbcTemplate template) {
        return new JdbcRequestUserLinksRepository(template);
    }

    @Bean
    public LinkService getLinkService(
            ClientManager clientManager,
            JdbcRequestLinkRepository jdbcRequestLinkRepository,
            JdbcRequestUserLinksRepository jdbcRequestUserLinksRepository) {
        return new JdbcLinkService(jdbcRequestLinkRepository, jdbcRequestUserLinksRepository, clientManager);
    }

    @Bean
    public TgChatService getTgChetService(
            JdbcRequestClientRepository jdbcRequestClientRepository) {
        return new JdbcTgChatService(jdbcRequestClientRepository);
    }
}