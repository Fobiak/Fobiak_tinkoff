package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.AddLinkResponse;
import ru.tinkoff.edu.java.bot.enums.InformationCommand;
import ru.tinkoff.edu.java.bot.web.client.ScrapperClient;


@Component
public class CommandTrack extends AbstractCommand {

    private ScrapperClient client;
    public CommandTrack(ScrapperClient client) {
        this.client = client;
        informationCommand = InformationCommand.TRACK;
    }

    @Override
    public SendMessage handle(Update update) {
        // User user = getDB
        // user.state = Track
        return new SendMessage(update.message().chat().id(), "Enter url for track");
    }

    @Override
    public SendMessage handleWithArgument(Update update) {
        // Add url
        String url = update.message().text();
        Mono<AddLinkResponse> response = client.addTrackedLink(new AddLinkRequest(update.message().chat().id(), url));
        System.out.println(response.block());
        return new SendMessage(update.message().chat().id(), "Add url for track");
    }
}
