package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.enums.InformationCommand;
import ru.tinkoff.edu.java.bot.web.client.ScrapperClient;

@Component
public class CommandStart extends AbstractCommand {

    private ScrapperClient client;
    public CommandStart(ScrapperClient client) {
        this.client = client;
        informationCommand = InformationCommand.START;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String userName = update.message().from().firstName() + " " + update.message().from().lastName();
        client.registerChat(chatId, userName);
        return new SendMessage(chatId, "Hello, " + userName + "! Enter /help or open Menu to find out what I can");
    }
}
