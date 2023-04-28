package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URI;
import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final LinkRepository linkRepository;

    @Override
    @Transactional
    public Link add(long tgChatId, URI url) {
        Link incomingLink = Link.builder()
                .url(url)
                .build();

        Link currLink = linkRepository.find(incomingLink);
        if (currLink == null) {
            currLink = linkRepository.save(incomingLink);
        }

        linkRepository.saveToChat(tgChatId, currLink.getId());
        return currLink;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI url) {
        Link incomingLink = Link.builder()
                .url(url)
                .build();

        Link link = linkRepository.find(incomingLink);
        if (link == null) {
            throw new NoSuchElementException("Такой ссылки не существует");
        }

        linkRepository.removeFromChat(tgChatId, link.getId());
        return link;
    }

    @Override
    @Transactional
    public Collection<Link> listAll(long tgChatId) {
        return linkRepository.findAllByChat(tgChatId);
    }
}
