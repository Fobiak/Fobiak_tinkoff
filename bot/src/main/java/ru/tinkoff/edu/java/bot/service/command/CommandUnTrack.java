package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.DeleteLinkRequest;
import ru.tinkoff.edu.java.bot.dto.DeleteLinkResponse;
import ru.tinkoff.edu.java.bot.enums.InformationCommand;
import ru.tinkoff.edu.java.bot.web.client.ScrapperClient;

@Component
public class CommandUnTrack extends AbstractCommand {
    private ScrapperClient client;
    public CommandUnTrack(ScrapperClient client) {
        this.client = client;
        informationCommand = InformationCommand.UNTRACK;
    }

    @Override
    public SendMessage handle(Update update) {
        // User user = getDB
        // user.state = UnTrack
        return new SendMessage(update.message().chat().id(), "Enter url for untrack");
    }

    @Override
    public SendMessage handleWithArgument(Update update) {
        // Delete url
        String url = update.message().text();
        // Detective url
        Mono<DeleteLinkResponse> response = client.deleteTrackedLink(new DeleteLinkRequest(update.message().chat().id(), url));
        return new SendMessage(update.message().chat().id(), "Delete tracked url");
    }
}
