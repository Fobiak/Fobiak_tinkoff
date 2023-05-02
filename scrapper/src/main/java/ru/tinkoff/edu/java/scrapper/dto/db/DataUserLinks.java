package ru.tinkoff.edu.java.scrapper.dto.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataUserLinks {
    private Long userId;
    private Long linksId;
}
