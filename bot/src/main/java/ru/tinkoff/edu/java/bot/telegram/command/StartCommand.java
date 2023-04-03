package ru.tinkoff.edu.java.bot.telegram.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand extends AbstractCommand{
    public StartCommand(CommandList command) {
        super(command);
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.getMessage()
                .getChatId().toString(), "Регистирую нового пользователя");
    }
}
