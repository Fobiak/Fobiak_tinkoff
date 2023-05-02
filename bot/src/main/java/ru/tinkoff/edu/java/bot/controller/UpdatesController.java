package ru.tinkoff.edu.java.bot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.Bot;

@RestController
@RequiredArgsConstructor
public class UpdatesController {

    private final Bot bot;

    @PostMapping("/updates")
    public void update(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        bot.mailingToUsers(linkUpdateRequest);
    }
}
