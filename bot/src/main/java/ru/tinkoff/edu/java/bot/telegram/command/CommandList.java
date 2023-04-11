package ru.tinkoff.edu.java.bot.telegram.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

public enum CommandList {
    START("start", "Зарегистировать пользователя"),
    HELP("help", "Показать окно с командами"),
    TRACK("track", "Начать отслеживание ссылки"),
    UNTRACK("untrack", "Прекратить отслеживание ссылки"),
    LIST("list", "Показать список отслеживаемых ссылок");

    private final String command;
    private final String desc;

    CommandList(String command, String desc) {
        this.command = command;
        this.desc = desc;
    }

    public boolean supports(Update update) {
        return update.getMessage()
                .getText()
                .startsWith(String.format("/%s", command));
    }

    public BotCommand toBotCommand() {
        return new BotCommand(command, desc);
    }
}
