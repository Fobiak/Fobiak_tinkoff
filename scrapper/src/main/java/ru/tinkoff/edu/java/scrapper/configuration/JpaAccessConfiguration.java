package ru.tinkoff.edu.java.scrapper.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.domain.jpa.JpaRequestClientRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.JpaRequestLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;
import ru.tinkoff.edu.java.scrapper.web.ClientManager;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public LinkService getLinkService(
            ClientManager clientManager,
            JpaRequestClientRepository jpaRequestClientRepository,
            JpaRequestLinkRepository jpaRequestLinkRepository) {
        return new JpaLinkService(clientManager, jpaRequestLinkRepository, jpaRequestClientRepository);
    }

    @Bean
    public TgChatService getTgChetService(
            JpaRequestClientRepository jpaRequestClientRepository) {
        return new JpaTgChatService(jpaRequestClientRepository);
    }
}
