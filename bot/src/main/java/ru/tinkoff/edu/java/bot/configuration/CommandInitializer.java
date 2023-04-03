package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.telegram.command.CommandList;
import ru.tinkoff.edu.java.bot.telegram.command.HelpCommand;
import ru.tinkoff.edu.java.bot.telegram.command.ListCommand;
import ru.tinkoff.edu.java.bot.telegram.command.StartCommand;
import ru.tinkoff.edu.java.bot.telegram.command.TrackCommand;
import ru.tinkoff.edu.java.bot.telegram.command.UntrackCommand;

@Component
public record CommandInitializer() {
    @Bean
    public StartCommand startCommand() {
        return new StartCommand(CommandList.START);
    }

    @Bean
    public HelpCommand helpCommand() {
        return new HelpCommand(CommandList.HELP);
    }

    @Bean
    public ListCommand listCommand() {
        return new ListCommand(CommandList.LIST);
    }

    @Bean
    public TrackCommand trackCommand() {
        return new TrackCommand(CommandList.TRACK);
    }

    @Bean
    public UntrackCommand untrackCommand() {
        return new UntrackCommand(CommandList.UNTRACK);
    }
}
