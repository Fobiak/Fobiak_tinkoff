package ru.tinkoff.edu.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
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
import ru.tinkoff.edu.java.scrapper.dto.entity.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URI;
import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/links",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class LinkController {
    private final LinkService linkService;

    @GetMapping(path = "/{id}")
    public ListLinksResponse getAll(@PathVariable long id) {
        List<LinkResponse> links = linkService.listAll(id).stream()
                .map(link -> new LinkResponse(link.getId(), link.getUrl().toString()))
                .toList();
        return new ListLinksResponse(links, links.size());
    }

    @PostMapping("/{id}")
    public LinkResponse add(@PathVariable long id, @RequestBody AddLinkRequest request) {
        Link link = linkService.add(id, URI.create(request.link()));
        return new LinkResponse(link.getId(), link.getUrl().toString());
    }

    @DeleteMapping("/{id}")
    public LinkResponse remove(@PathVariable long id, @RequestBody RemoveLinkRequest request) {
        Link link = linkService.remove(id, URI.create(request.link()));
        return new LinkResponse(link.getId(), link.getUrl().toString());
    }
}
