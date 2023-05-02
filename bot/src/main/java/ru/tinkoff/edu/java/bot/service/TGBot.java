package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.dto.UserGetResponse;
import ru.tinkoff.edu.java.bot.service.command.Command;
import ru.tinkoff.edu.java.bot.enums.StateUser;
import ru.tinkoff.edu.java.bot.service.models.User;
import ru.tinkoff.edu.java.bot.web.client.ScrapperClient;


import java.util.List;

@Service
public class TGBot implements Bot {
    private final TelegramBot bot;
    private final List<Command> commands;

    private ScrapperClient scrapperClient;

    public TGBot(@Value("#{getBotToken}") String token, List<Command> commands, ScrapperClient scrapperClient) {
        this.commands = commands;
        bot = new TelegramBot(token);
        bot.setUpdatesListener(this);
        bot.execute(new SetMyCommands(commands.stream().map(x -> new BotCommand(x.command(), x.description()))
                .toArray(BotCommand[]::new)));
        this.scrapperClient = scrapperClient;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        if (request != null) {
            SendResponse sendResponse = (SendResponse) bot.execute(request);
            boolean ok = sendResponse.isOk();
            Message message = sendResponse.message();
        }
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            SendMessage request = null;
            Long chatId = update.message().chat().id();
            if (update.message().entities() != null && update.message().entities()[0].type() == MessageEntity.Type.bot_command) {
                String comm = update.message().text();
                for (Command command : commands) {
                    if (command.command().equals(comm) ){
                        request = command.handle(update);
                        execute(request);
                        scrapperClient.updateUser(chatId, command.getState());
                        break;
                    }
                }
                if (request == null) {
                    execute(new SendMessage(chatId, "Unidentified command"));
                }
            } else {
                UserGetResponse userGetResponse = scrapperClient.getUser(chatId).block();
                if (userGetResponse != null) {
                    User user = new User(userGetResponse.id(), userGetResponse.userName(),
                            StateUser.valueOf(userGetResponse.userState()));
                    if (user.userState() != StateUser.NONE) {
                        for (Command command : commands) {
                            if (command.getState() == user.userState()) {
                                request = command.handleWithArgument(update);
                                execute(request);
                                break;
                            }
                        }
                    }
                }

            }
        }
        return CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void mailingToUsers(LinkUpdateRequest linkUpdateRequest) {
        for (Long chatId : linkUpdateRequest.tgChatIds()) {
            execute(new SendMessage(chatId,
                    linkUpdateRequest.url() + " update! " + linkUpdateRequest.description()));
        }
    }
}
