package ru.tinkoff.edu.java.scrapper.web.sheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private static final Logger log =
            LoggerFactory.getLogger(LinkUpdaterScheduler.class);

    private final LinkUpdater linkUpdaterImpl;

    @Scheduled(fixedDelayString = "#{schedulerIntervalMs}")
    public void update() {
        int countUpdate = linkUpdaterImpl.update();
        log.info("Update info about urls. Count Link update: " + countUpdate);
    }
}
