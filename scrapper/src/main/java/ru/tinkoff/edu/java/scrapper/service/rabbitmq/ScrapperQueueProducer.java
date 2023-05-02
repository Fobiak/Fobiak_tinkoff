package ru.tinkoff.edu.java.scrapper.service.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

@Service
public class ScrapperQueueProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String EXCHANGE;
    private final String ROUTE_KEY;

    public ScrapperQueueProducer(RabbitTemplate rabbitTemplate, ApplicationConfig config) {
        this.rabbitTemplate = rabbitTemplate;
        EXCHANGE = config.exchange();
        ROUTE_KEY = config.queue();
    }

    public void send(LinkUpdateRequest update) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTE_KEY, update);
    }
}