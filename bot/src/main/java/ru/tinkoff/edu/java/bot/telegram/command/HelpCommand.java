package ru.tinkoff.edu.java.bot.telegram.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Order(2)

public class HelpCommand extends AbstractCommand {
    private static final String MSG = "Помогаю";

    public HelpCommand() {
        super(CommandList.HELP);
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.getMessage()
                .getChatId().toString(), MSG);
    }
}
