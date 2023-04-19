package ru.tinkoff.edu.java.scrapper.repository;

import ru.tinkoff.edu.java.scrapper.dto.entity.Chat;

import java.util.List;

public interface ChatRepository {
    Chat save(Chat chat);

    boolean removeById(long id);

    Chat findById(long id);

    List<Chat> findAll();

    List<Chat> findAllByLink(long linkId);
}
