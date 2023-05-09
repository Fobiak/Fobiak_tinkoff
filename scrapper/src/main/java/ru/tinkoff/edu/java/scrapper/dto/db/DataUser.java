package ru.tinkoff.edu.java.scrapper.dto.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataUser {
    private Long chatId;
    private String userName;
}
