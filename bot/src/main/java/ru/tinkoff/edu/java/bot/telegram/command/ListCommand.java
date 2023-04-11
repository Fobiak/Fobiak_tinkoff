package ru.tinkoff.edu.java.bot.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tinkoff.edu.java.bot.dto.client.ListLinksResponse;
import ru.tinkoff.edu.java.bot.client.ScrapperClientImpl;


@Component
@Order(5)
public class ListCommand extends AbstractCommand {
    private static final String NO_LINKS_MSG = "Нет отслеживаемых ссылок";
    private static final String THERE_ARE_LINKS_MSG = "Список отслеживаемых ссылок:\n";
    private ScrapperClientImpl client;

    public ListCommand() {
        super(CommandList.LIST);
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        ListLinksResponse response = client.getAllLinks(chatId);
        return new SendMessage(chatId.toString(),
                response.size() == 0 ? NO_LINKS_MSG : THERE_ARE_LINKS_MSG + response.links().stream()
                        .map(link -> link.url() + "\n")
                        .reduce("", String::concat));
    }

    @Autowired
    public void setClient(ScrapperClientImpl client) {
        this.client = client;
    }
}

