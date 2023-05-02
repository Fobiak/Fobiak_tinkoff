package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.enums.StateUser;

public interface Command {
    String command();

    String description();

    SendMessage handle(Update update);

    StateUser getState();

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }

    default SendMessage handleWithArgument(Update update) {
        return null;
    }
}
