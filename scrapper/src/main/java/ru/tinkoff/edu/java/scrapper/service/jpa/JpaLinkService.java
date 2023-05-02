package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.app.ParsingUrlService;
import ru.tinkoff.app.url.UrlData;
import ru.tinkoff.edu.java.scrapper.domain.jpa.JpaRequestClientRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.JpaRequestLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.ClientEntity;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLink;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLinkWithInformation;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUserLinks;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.web.ClientManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final ClientManager clientManager;
    private final JpaRequestLinkRepository jpaRequestLinkRepository;
    private final JpaRequestClientRepository jpaRequestClientRepository;

    @Transactional
    @Override
    public DataLink add(long tgChatId, URI url) {
        List<LinkEntity> dataLinks = findLinkByUrl(url);
        if (dataLinks.size() > 0) {
            LinkEntity result = dataLinks.get(0);
            jpaRequestClientRepository.getReferenceById(tgChatId).getUserLinks()
                    .add(result);
            return new DataLink(result.getId(), url);
        }

        UrlData urlData = ParsingUrlService.getInfoAboutURL(url.toString());
        if (urlData == null) {
            return null;
        }
        ClientEntity client = jpaRequestClientRepository.getReferenceById(tgChatId);

        OffsetDateTime timeEditLast = clientManager.timeEditLinkForType(urlData);
        Integer count = clientManager.getCountAnswer(urlData);

        LinkEntity linkEntity = new LinkEntity(url.toString(), OffsetDateTime.now(), timeEditLast, count);
        linkEntity.getUsers().add(client);
        LinkEntity link = jpaRequestLinkRepository.save(linkEntity);

        client.getUserLinks().add(link);
        jpaRequestClientRepository.save(client);
        return new DataLink(link.getId(), url);
    }

    @Transactional
    @Override
    public DataLink remove(long tgChatId, URI url) {
        ClientEntity client = jpaRequestClientRepository.getReferenceById(tgChatId);
        LinkEntity link = findLinkByUrl(url).get(0);
        client.getUserLinks().remove(link);
        return new DataLink(link.getId(), url);
    }

    @Override
    public Collection<DataLink> listLinkAll(long tgChatId) {
        return jpaRequestClientRepository.getReferenceById(tgChatId)
                .getUserLinks().stream().map(x -> {
                    try {
                        return new DataLink(x.getId(), new URI(x.getUrl()));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    @Override
    public List<DataLinkWithInformation> listLongTimeUpdate() {
        return jpaRequestLinkRepository.findAll().stream().map(x -> {
            try {
                return new DataLinkWithInformation(x.getId(),
                        new URI(x.getUrl()), x.getLastUpdate(),
                        x.getLastEditTime(), x.getCountAnswer());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @Transactional
    @Override
    public List<DataUserLinks> findUserLinksByLinks(long idLink) {
        return jpaRequestLinkRepository.getReferenceById(idLink).getUsers()
                .stream().map(x -> new DataUserLinks(x.getId(), idLink)).toList();
    }

    private List<LinkEntity> findLinkByUrl(URI url) {
        return jpaRequestLinkRepository.getLinkEntityByUrl(url.toString());
    }
}
