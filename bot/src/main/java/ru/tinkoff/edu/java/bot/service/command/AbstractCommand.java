package ru.tinkoff.edu.java.bot.service.command;

import ru.tinkoff.edu.java.bot.enums.InformationCommand;
import ru.tinkoff.edu.java.bot.enums.StateUser;

public abstract class AbstractCommand implements Command {
    protected InformationCommand informationCommand;

    @Override
    public String command() {
        return informationCommand.getName();
    }

    @Override
    public String description() {
        return informationCommand.getDescription();
    }

    @Override
    public StateUser getState() {
        return informationCommand.getStateUser();
    }
}
