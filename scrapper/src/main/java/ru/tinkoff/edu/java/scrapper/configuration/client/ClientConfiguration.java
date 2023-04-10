package ru.tinkoff.edu.java.scrapper.configuration.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClientImpl;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClientImpl;

@Configuration
public class ClientConfiguration {
        private final String githubBaseUrl;
        private final String stackOverflowBaseUrl;

        public ClientConfiguration(@Value("${client.base-url.github}") String githubBaseUrl,
                                   @Value("${client.base-url.stackoverflow}") String stackOverflowBaseUrl) {
                this.githubBaseUrl = githubBaseUrl;
                this.stackOverflowBaseUrl = stackOverflowBaseUrl;
        }

        @Bean
        public GitHubClient gitHubClient() {
                return new GitHubClientImpl(githubBaseUrl);
        }

        @Bean
        public StackOverflowClient stackOverflowClient() {
                return new StackOverflowClientImpl(stackOverflowBaseUrl);
        }

        @Bean
        public long schedulerIntervalMs(ApplicationConfig config) {
                return config.scheduler().interval().toMillis();
        }
}