package ru.tinkoff.edu.java.bot.telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tinkoff.edu.java.bot.telegram.command.Command;

import java.util.List;

@Service
public class UpdateHandler {
    private static final String UNSUPPORTED_COMMAND_RESPONSE = "Эта команда не поддерживается. Введите /help";
    private final List<Command> commandList;

    public UpdateHandler(List<Command> commandList) {
        this.commandList = commandList;
    }

    public SendMessage handle(Update update) {
        return commandList.stream()
                .filter(command -> command.supports(update))
                .map(command -> command.handle(update))
                .findFirst()
                .orElse(new SendMessage(update.getMessage()
                        .getChatId().toString(), UNSUPPORTED_COMMAND_RESPONSE));
    }

    public List<Command> getCommandList() {
        return commandList;
    }
}