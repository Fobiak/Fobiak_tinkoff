package ru.tinkoff.edu.java.scrapper.domain.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "link")
@AllArgsConstructor
@NoArgsConstructor
public class LinkEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    @Column(name = "last_edit_time", nullable = false)
    private OffsetDateTime lastEditTime;

    @Column(name = "count_commit_or_question", nullable = false)
    private Integer countAnswer;

    @ManyToMany(mappedBy = "userLinks")
    private Set<ClientEntity> users = new HashSet<>();

    public LinkEntity(String url, OffsetDateTime lastUpdate, OffsetDateTime lastEditTime, Integer countAnswer) {
        this.url = url;
        this.lastEditTime = lastEditTime;
        this.lastUpdate = lastUpdate;
        this.countAnswer = countAnswer;
    }
}
