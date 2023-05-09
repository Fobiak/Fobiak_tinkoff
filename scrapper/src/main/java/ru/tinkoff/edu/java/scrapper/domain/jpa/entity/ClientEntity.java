package ru.tinkoff.edu.java.scrapper.domain.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client")
public class ClientEntity  {

    @Id
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_state", nullable = false)
    @NotNull
    private String userState;


    @ManyToMany
    @JoinTable(
            name = "user_links",
            joinColumns = @JoinColumn(name = "links_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<LinkEntity> userLinks = new HashSet<>();

}
