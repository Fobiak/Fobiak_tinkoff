package ru.tinkoff.edu.java.scrapper.repository;

import ru.tinkoff.edu.java.scrapper.dto.entity.Link;

import java.util.List;

public interface LinkRepository {
    Link save(Link link);

    boolean removeById(long id);

    boolean remove(Link link);

    Link findById(long id);

    Link find(Link link);

    List<Link> findAll();

    boolean saveToChat(long linkId, long chatId);

    boolean removeFromChat(long linkId, long chatId);

    List<Link> findAllByChat(long chatId);
}
