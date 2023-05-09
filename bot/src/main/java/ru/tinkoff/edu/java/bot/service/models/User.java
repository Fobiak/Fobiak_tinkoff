package ru.tinkoff.edu.java.bot.service.models;

import ru.tinkoff.edu.java.bot.enums.StateUser;

public record User (Long chatId, String userName, StateUser userState) { }
