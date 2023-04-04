import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tinkoff.edu.java.bot.telegram.UpdateHandler;
import ru.tinkoff.edu.java.bot.telegram.command.Command;
import ru.tinkoff.edu.java.bot.telegram.command.HelpCommand;
import ru.tinkoff.edu.java.bot.telegram.command.ListCommand;
import ru.tinkoff.edu.java.bot.telegram.command.StartCommand;
import ru.tinkoff.edu.java.bot.telegram.command.TrackCommand;
import ru.tinkoff.edu.java.bot.telegram.command.UntrackCommand;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnsupportedCommandTest {
    private static UpdateHandler handler;

    @BeforeAll
    public static void setUp() {
        List<Command> commandList = new ArrayList<>(List.of(
                new StartCommand(),
                new HelpCommand(),
                new ListCommand(),
                new TrackCommand(),
                new UntrackCommand()));
        handler = new UpdateHandler(commandList);
    }

    @Test
    public void when_EnterUnsupportedCommand_Expect_SpecialMessage() {
        SendMessage response = handler.handle(getUpdate());
        assertEquals("Эта команда не поддерживается. Введите /help", response.getText());
    }

    private Update getUpdate() {
        Update update = new Update();
        Message msg = new Message();
        msg.setChat(new Chat(0L, "secret"));
        msg.setText("/boo");
        update.setMessage(msg);
        return update;
    }
}
