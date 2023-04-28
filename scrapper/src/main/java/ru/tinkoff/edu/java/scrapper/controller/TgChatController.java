package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/tg-chat",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TgChatController {
    private final TgChatService chatService;

    @PostMapping("/{id}")
    public void register(@PathVariable @PositiveOrZero long id) {
        chatService.register(id);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable @PositiveOrZero long id) {
        chatService.unregister(id);
    }
}
