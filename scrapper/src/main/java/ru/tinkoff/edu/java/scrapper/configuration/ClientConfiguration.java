package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.web.ClientManager;
import ru.tinkoff.edu.java.scrapper.web.client.*;


@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClient getGitHubClient() {
        return new GitHubClientImpl();
    }

    @Bean
    public StackOverflowClient getStackOverflowClient() {
        return new StackOverflowClientImpl();
    }

    @Bean
    public BotClient getBotClient() {
        return new BotClientImpl();
    }

    @Bean
    public long schedulerIntervalMs(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }

    @Bean
    public ClientManager getClientManager() {
        return new ClientManager(getGitHubClient(), getStackOverflowClient());
    }
}
