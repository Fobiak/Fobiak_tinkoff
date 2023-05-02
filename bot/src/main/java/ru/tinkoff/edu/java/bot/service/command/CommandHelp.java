package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.enums.InformationCommand;

import java.util.List;

@Component
public class CommandHelp extends AbstractCommand {

    private List<Command> commands;

    public CommandHelp(List<Command> commands) {
        this.commands = commands;
        informationCommand = InformationCommand.HELP;
    }
    @Override
    public SendMessage handle(Update update) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Command command : commands) {
            stringBuilder.append(command.command()).append(" ").append(command.description()).append("\n");
        }
        return new SendMessage(update.message().chat().id(), stringBuilder.toString());
    }
}
