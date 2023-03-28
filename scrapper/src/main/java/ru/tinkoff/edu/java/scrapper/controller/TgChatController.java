package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;

import java.util.NoSuchElementException;

@Validated
@RestController
@RequestMapping(path = "/tg-chat",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TgChatController {
    @PostMapping("/{id}")
    public LinkResponse register(@PathVariable @PositiveOrZero long id) {
        return new LinkResponse(id, "tcsbank.ru/some-secrets");
    }

    @DeleteMapping("/{id}")
    public LinkResponse remove(@PathVariable @PositiveOrZero long id) {
        throw new NoSuchElementException(String.format("Чат c id={%d} не существует", id));
    }
}
