package ru.tinkoff.edu.java.scrapper.configuration;

import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.domain.jooq.JooqRequestClientRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.JooqRequestLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.JooqRequestUserLinksRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JoopTgChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.web.ClientManager;


@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {

    @Bean
    public JooqRequestClientRepository getJooqRequestClientRepository(DSLContext dslContext) {
        return new JooqRequestClientRepository(dslContext);
    }

    @Bean
    public JooqRequestLinkRepository getJooqRequestLinkRepository(DSLContext dslContext) {
        return new JooqRequestLinkRepository(dslContext);
    }

    @Bean
    public JooqRequestUserLinksRepository getJooqRequestUserLinksRepository(DSLContext dslContext) {
        return new JooqRequestUserLinksRepository(dslContext);
    }

    @Bean
    public LinkService getLinkService(
            ClientManager clientManager,
            JooqRequestLinkRepository jooqRequestLinkRepository,
            JooqRequestUserLinksRepository jooqRequestUserLinksRepository) {
        return new JooqLinkService(clientManager, jooqRequestLinkRepository, jooqRequestUserLinksRepository);
    }

    @Bean
    public TgChatService getTgChetService(
            JooqRequestClientRepository jooqRequestClientRepository) {
        return new JoopTgChatService(jooqRequestClientRepository);
    }
}