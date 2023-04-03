package ru.tinkoff.edu.java.bot.telegram;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.telegram.command.Command;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final ApplicationConfig config;
    private final List<Command> commandList;
    private final UpdateHandler updateHandler;

    public TelegramBot(ApplicationConfig config, UpdateHandler updateHandler) {
        this.config = config;
        this.updateHandler = updateHandler;
        this.commandList = updateHandler.getCommandList();
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        List<BotCommand> botCommands = commandList.stream()
                .map(Command::toBotCommand)
                .toList();
        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(botCommands);
        execute(setMyCommands);
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            execute(updateHandler.handle(update));
        }
    }

    @Override
    public String getBotUsername() {
        return config.username();
    }

    @Override
    public String getBotToken() {
        return config.token();
    }
}