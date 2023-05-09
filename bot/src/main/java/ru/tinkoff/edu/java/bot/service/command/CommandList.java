package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.ListLinkRequest;
import ru.tinkoff.edu.java.bot.dto.ListLinkResponse;
import ru.tinkoff.edu.java.bot.enums.InformationCommand;
import ru.tinkoff.edu.java.bot.web.client.ScrapperClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class CommandList extends AbstractCommand {

    private ScrapperClient client;
    public CommandList(ScrapperClient client) {
        this.client = client;
        informationCommand = InformationCommand.LIST;
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder stringBuilder = new StringBuilder();
        Mono<ListLinkResponse> response = client.listTrackedLink(new ListLinkRequest(update.message().chat().id()));
        ListLinkResponse listLinkResponse = response.block();
        if (listLinkResponse != null) {
            List<URI> urls = listLinkResponse.links().stream().map(x -> {
                try {
                    return new URI(x.url());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
            if (urls.size() > 0) {
                for (URI url : urls) {
                    stringBuilder.append(url.toString()).append("\n");
                }
            } else {
                stringBuilder.append("No tracked links");
            }
        } else {
            stringBuilder.append("Server is not responding");
        }
        return new SendMessage(update.message().chat().id(), stringBuilder.toString());
    }
}
