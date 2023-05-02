package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

import java.util.List;

public interface Bot extends UpdatesListener {
    <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request);

    @Override
    int process(List<Update> updates);

    void mailingToUsers(LinkUpdateRequest linkUpdateRequest);
}