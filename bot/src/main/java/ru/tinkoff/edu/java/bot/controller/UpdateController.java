package ru.tinkoff.edu.java.bot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateResponse;

@Validated
@RestController
@RequestMapping(path = "/updates",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UpdateController {

    @PostMapping
    public LinkUpdateResponse sendUpdate(@RequestBody LinkUpdateRequest request) {
        return new LinkUpdateResponse("Обновление обработано", HttpStatus.OK);
    }
}