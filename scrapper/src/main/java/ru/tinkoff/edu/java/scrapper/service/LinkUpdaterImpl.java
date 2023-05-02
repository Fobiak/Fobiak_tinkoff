package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.app.ParsingUrlService;
import ru.tinkoff.app.url.UrlData;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.db.DataLinkWithInformation;
import ru.tinkoff.edu.java.scrapper.dto.db.DataUserLinks;
import ru.tinkoff.edu.java.scrapper.web.ClientManager;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LinkUpdaterImpl implements LinkUpdater {

    private final LinkService linkService;

    private final SendNotification sendNotification;
    private final ClientManager clientManager;

    @Override
    public int update() {
        List<DataLinkWithInformation> dataLinkWithInformation = linkService.listLongTimeUpdate();
        int countUpdate = 0;
        for (DataLinkWithInformation data : dataLinkWithInformation) {
            UrlData urlData = ParsingUrlService.getInfoAboutURL(data.getUrl().toString());
            if (urlData == null) {
                continue;
            }

            OffsetDateTime timeEdit = clientManager.timeEditLinkForType(urlData);
            if (!timeEdit.equals(data.getLastEditTime())) {
                Integer countAnswerNow = clientManager.getCountAnswer(urlData);
                String description = "";
                if (countAnswerNow > data.getCountAnswer()) {
                    description = clientManager.getInfoCountByType(urlData.getType());
                }
                List<DataUserLinks> dataUserLinks = linkService.findUserLinksByLinks(data.getId());
                sendNotification.sendRequest(
                        new LinkUpdateRequest(
                                data.getId(), data.getUrl(), description,
                                dataUserLinks.stream().map(DataUserLinks::getUserId).toList()
                        ));
                countUpdate++;
            }
        }
        return countUpdate;
    }
}
