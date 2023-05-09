package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.web.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.web.client.ScrapperClientImpl;

@Configuration
public class ClientConfiguration {

    @Bean
    public ScrapperClient getScrapperClient() {
        return new ScrapperClientImpl();
    }
}

