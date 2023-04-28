package ru.tinkoff.edu.java.bot.telegram.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

public abstract class AbstractCommand implements Command {
    private final CommandList command;

    public AbstractCommand(CommandList command) {
        this.command = command;
    }

    @Override
    public boolean supports(Update update) {
        return command.supports(update);
    }

    @Override
    public BotCommand toBotCommand() {
        return command.toBotCommand();
    }
}
