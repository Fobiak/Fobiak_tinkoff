package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.app.ParsingUrlService;
import ru.tinkoff.app.url.UrlData;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.JdbcRequestLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.JdbcRequestUserLinksRepository;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLink;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLinkWithInformation;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUserLinks;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.web.ClientManager;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private static final int COUNT_LINK_LIMIT = 10;
    private final JdbcRequestLinkRepository jdbcRequestLink;
    private final JdbcRequestUserLinksRepository jdbcRequestUserLinks;
    
    private final ClientManager clientManager;


    @Override
    public DataLink add(long tgChatId, URI url) {
        UrlData urlData = ParsingUrlService.getInfoAboutURL(url.toString());
        if (urlData == null) {
            return null;
        }
        OffsetDateTime timeEditLast = clientManager.timeEditLinkForType(urlData);
        Integer count = clientManager.getCountAnswer(urlData);
        DataLink dataLink = jdbcRequestLink.addLink(url, timeEditLast, count);
        jdbcRequestUserLinks.addUserLink(tgChatId, dataLink.getId());
        return dataLink;
    }

    @Override
    public DataLink remove(long tgChatId, URI url) {
        DataLink dataLink = jdbcRequestLink.removeLink(url);
        jdbcRequestUserLinks.removeLink(tgChatId, dataLink.getId());
        List<DataUserLinks> dataUserLinksList = jdbcRequestUserLinks.findUserLinksByLink(dataLink.getId());
        if (dataUserLinksList.size() == 0) {
            jdbcRequestLink.removeLink(url);
        }
        return dataLink;
    }

    @Override
    public Collection<DataLink> listLinkAll(long tgChatId) {
        List<DataUserLinks> dataUserLinksList = jdbcRequestUserLinks.findUserLinksByUser(tgChatId);
        Set<Long> setLinksId = dataUserLinksList.stream().map(DataUserLinks::getLinksId).collect(Collectors.toSet());
        List<DataLink> dataLinks = jdbcRequestLink.findAllLinks();
        return dataLinks.stream().filter(x -> setLinksId.contains(x.getId())).toList();
    }

    @Override
    public List<DataLinkWithInformation> listLongTimeUpdate() {
        return jdbcRequestLink.findLinkNotUpdateLongTime(COUNT_LINK_LIMIT);
    }

    @Override
    public List<DataUserLinks> findUserLinksByLinks(long idLink) {
        return jdbcRequestUserLinks.findUserLinksByLink(idLink);
    }

}
