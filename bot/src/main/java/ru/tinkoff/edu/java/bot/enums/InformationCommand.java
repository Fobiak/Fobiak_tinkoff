package ru.tinkoff.edu.java.bot.enums;

public enum InformationCommand {
    START("/start", "Start of work with bot", StateUser.NONE),
    HELP("/help", "Information about commands", StateUser.NONE),
    LIST("/list", "Get lists tracked urls", StateUser.NONE),
    TRACK("/track", "Add track url", StateUser.TRACK),
    UNTRACK("/untrack", "Delete tracked url", StateUser.UNTRACK);

    private final String name;
    private final String description;

    private final StateUser stateUser;

    InformationCommand(String name, String description, StateUser stateUser) {
        this.name = name;
        this.description = description;
        this.stateUser = stateUser;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StateUser getStateUser() {
        return stateUser;
    }
}
