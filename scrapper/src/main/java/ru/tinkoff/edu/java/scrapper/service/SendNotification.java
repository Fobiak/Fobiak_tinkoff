package ru.tinkoff.edu.java.scrapper.service;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.service.rabbitmq.ScrapperQueueProducer;
import ru.tinkoff.edu.java.scrapper.web.client.BotClient;

@Service
public class SendNotification {

    private final BotClient botClient;
    private final ScrapperQueueProducer scrapperQueueProducer;
    private final boolean useQueue;

    public SendNotification(BotClient botClient, ScrapperQueueProducer scrapperQueueProducer, ApplicationConfig config) {
        this.botClient = botClient;
        this.scrapperQueueProducer = scrapperQueueProducer;
        this.useQueue = config.useQueue();
    }

    public void sendRequest(LinkUpdateRequest request) {
        System.out.println(useQueue);
        if (useQueue) {
            scrapperQueueProducer.send(request);
        } else {
            botClient.updater(request);
        }
    }

}
