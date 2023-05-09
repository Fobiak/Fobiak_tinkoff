package ru.tinkoff.edu.java.scrapper.web.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoteLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLink;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
public class LinksController {

    private final LinkService linkService;

    public LinksController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping(path = "/links", produces = APPLICATION_JSON_VALUE)
    public ListLinkResponse getLinks(@RequestParam("Tg-Chat-Id") long idChat) {
        Collection<DataLink> dataLinks = linkService.listLinkAll(idChat);
        List<LinkResponse> linkResponses = dataLinks.stream().map(x -> new LinkResponse(x.getId(), x.getUrl().toString())).toList();
        return new ListLinkResponse(linkResponses, linkResponses.size());
    }

    @PostMapping(path = "links", produces = APPLICATION_JSON_VALUE)
    public AddLinkRequest addLink(@RequestParam("Tg-Chat-Id") long idChat, @RequestBody String addUrl) throws URISyntaxException {
        linkService.add(idChat, new URI(addUrl));
        return new AddLinkRequest(addUrl);
    }

    @DeleteMapping(path = "links", produces = APPLICATION_JSON_VALUE)
    public RemoteLinkResponse deleteLink(@RequestParam("Tg-Chat-Id") long idChat, @RequestBody String deleteUrl) throws URISyntaxException {
        linkService.remove(idChat, new URI(deleteUrl));
        return new RemoteLinkResponse(deleteUrl);
    }
}
