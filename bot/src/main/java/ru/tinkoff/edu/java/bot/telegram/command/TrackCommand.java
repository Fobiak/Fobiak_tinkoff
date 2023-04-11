package ru.tinkoff.edu.java.bot.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tinkoff.edu.java.bot.client.ScrapperClientImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@Order(3)
public class TrackCommand extends AbstractCommand {
    private static final String MSG = "Начинаю отслеживание ссылки";
    private static final String ERROR_MSG = "Использован неверный формат. Используйте: /track <ссылка>";
    private static final Pattern COMMAND_PATTERN = Pattern.compile("^\\s*/track\\s+(\\S+)$");

    private ScrapperClientImpl client;

    public TrackCommand() {
        super(CommandList.TRACK);
    }

    @Override
    public SendMessage handle(Update update) {
        Message msg = update.getMessage();
        long chatId = msg.getChatId();

        Matcher matcher = COMMAND_PATTERN.matcher(msg.getText());
        if (!matcher.matches()) {
            return new SendMessage(String.valueOf(chatId), ERROR_MSG);
        }

        String url = matcher.group(1);
        client.addLink(chatId, url);
        return new SendMessage(String.valueOf(chatId), MSG);
    }

    @Autowired
    public void setClient(ScrapperClientImpl client) {
        this.client = client;
    }
}
