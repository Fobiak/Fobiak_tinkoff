package ru.tinkoff.edu.java.scrapper.dto.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataLink {
    private Long id;
    private URI url;
}
