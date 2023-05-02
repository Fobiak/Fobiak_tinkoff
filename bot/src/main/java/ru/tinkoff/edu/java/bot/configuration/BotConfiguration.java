package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

    @Bean
    public String getBotToken(ApplicationConfig config) {
        return config.botToken();
    }
}
