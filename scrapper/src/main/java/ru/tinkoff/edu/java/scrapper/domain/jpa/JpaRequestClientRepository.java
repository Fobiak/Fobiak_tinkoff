package ru.tinkoff.edu.java.scrapper.domain.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.ClientEntity;

public interface JpaRequestClientRepository extends JpaRepository<ClientEntity, Long> {

    void deleteClientEntitiesById(Long id);
}
