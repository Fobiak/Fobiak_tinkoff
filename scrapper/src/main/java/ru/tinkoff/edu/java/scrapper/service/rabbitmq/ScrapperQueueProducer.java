package ru.tinkoff.edu.java.scrapper.service.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;

@Service
public class ScrapperQueueProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String EXCHANGE;

    public ScrapperQueueProducer(RabbitTemplate rabbitTemplate, ApplicationConfig config) {
        this.rabbitTemplate = rabbitTemplate;
        EXCHANGE = config.exchange();
    }

    public void send(LinkUpdateRequest update) {
        rabbitTemplate.convertAndSend(EXCHANGE, update);
    }
}