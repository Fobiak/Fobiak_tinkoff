package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Validated
@RestController
@RequestMapping(path = "/links",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class LinkController {
    @GetMapping("/{id}")
    public ListLinksResponse getAll(@PathVariable long id) {
        return new ListLinksResponse(new ArrayList<>(), 0);
    }

    @PostMapping("/{id}")
    public LinkResponse add(@PathVariable long id, @RequestBody AddLinkRequest request) {
        return new LinkResponse(id, "tcsbank.ru/some-secrets");
    }

    @DeleteMapping("/{id}")
    public LinkResponse remove(@PathVariable long id, @RequestBody RemoveLinkRequest request) {
        throw new NoSuchElementException(String.format("Ссылка c id={%d} не найдена", id));
    }
}
