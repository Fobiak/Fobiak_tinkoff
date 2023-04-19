package ru.tinkoff.edu.java.scrapper.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    private Long id;
    private URI url;
    private Instant updatedAt;
}
