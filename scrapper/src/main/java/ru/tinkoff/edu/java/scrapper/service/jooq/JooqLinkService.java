package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.app.ParsingUrlService;
import ru.tinkoff.app.url.UrlData;
import ru.tinkoff.edu.java.scrapper.domain.jooq.JooqRequestLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.JooqRequestUserLinksRepository;
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
public class JooqLinkService implements LinkService {

    private static final int COUNT_LINK_LIMIT = 10;
    private final ClientManager clientManager;

    private final JooqRequestLinkRepository jooqRequestLink;
    private final JooqRequestUserLinksRepository jooqRequestUserLinks;

    @Override
    public DataLink add(long tgChatId, URI url) {
        UrlData urlData = ParsingUrlService.getInfoAboutURL(url.toString());
        if (urlData == null) {
            return null;
        }
        OffsetDateTime timeEditLast = clientManager.timeEditLinkForType(urlData);
        Integer count = clientManager.getCountAnswer(urlData);
        DataLink dataLink = jooqRequestLink.addLink(url, timeEditLast, count);
        jooqRequestUserLinks.addUserLink(tgChatId, dataLink.getId());
        return dataLink;
    }

    @Override
    public DataLink remove(long tgChatId, URI url) {
        DataLink dataLink = jooqRequestLink.removeLink(url);
        jooqRequestUserLinks.removeLink(tgChatId, dataLink.getId());
        List<DataUserLinks> dataUserLinksList = jooqRequestUserLinks.findUserLinksByLink(dataLink.getId());
        if (dataUserLinksList.size() == 0) {
            jooqRequestLink.removeLink(url);
        }
        return dataLink;
    }

    @Override
    public Collection<DataLink> listLinkAll(long tgChatId) {
        List<DataUserLinks> dataUserLinksList = jooqRequestUserLinks.findUserLinksByUser(tgChatId);
        Set<Long> setLinksId = dataUserLinksList.stream().map(DataUserLinks::getLinksId).collect(Collectors.toSet());
        List<DataLink> dataLinks = jooqRequestLink.findAllLinks();
        return dataLinks.stream().filter(x -> setLinksId.contains(x.getId())).toList();
    }

    @Override
    public List<DataLinkWithInformation> listLongTimeUpdate() {
        return jooqRequestLink.findLinkNotUpdateLongTime(COUNT_LINK_LIMIT);
    }

    @Override
    public List<DataUserLinks> findUserLinksByLinks(long idLink) {
        return jooqRequestUserLinks.findUserLinksByLink(idLink);
    }
}
