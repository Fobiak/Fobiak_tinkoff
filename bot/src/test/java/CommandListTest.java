

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.service.command.CommandList;
import ru.tinkoff.edu.java.bot.web.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.ListLinkRequest;
import ru.tinkoff.edu.java.bot.dto.ListLinkResponse;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommandListTest {

    private final  String data = """ 
            {'update_id': 557926017, 'message': {'message_id': 158, 'chat': {'id': 1296945790,
            'type': 'Private', 'first_name': 'Sergey', 'last_name': 'Kuznetsov'}, 'text': '/start', 'entities': [{'type': 'bot_command'}]}}""";
    private final Gson gson = new Gson();
    @Mock
    private ScrapperClient scrapperClient;

    @InjectMocks
    private CommandList commandList;

    @Test
    void handle__scrapperClientReturnEmptyList_ReturnSpecialMessage() {
        // given
        ListLinkRequest request = new ListLinkRequest(1296945790L);
        ListLinkResponse emptyList = new ListLinkResponse(List.of(), 0);
        Update update = gson.fromJson(data, Update.class);
        when(scrapperClient.listTrackedLink(request))
                .thenReturn(Mono.just(emptyList));

        // when
        SendMessage sendMessage = commandList.handle(update);
        Map<String, Object> result = sendMessage.getParameters();

        // then
        assertAll(
                () -> assertNotNull(result.get("chat_id")),
                () -> assertNotNull(result.get("text")),
                () -> assertEquals(result.get("chat_id"), 1296945790L),
                () -> assertEquals(result.get("text"), "No tracked links")
        );
    }

    @Test
    void handle__scrapperClientReturnListUrl_FormatMatches() {
        // given
        ListLinkRequest request = new ListLinkRequest(1296945790L);
        ListLinkResponse emptyList = new ListLinkResponse(List.of(), 0);
        Update update = gson.fromJson(data, Update.class);
        when(scrapperClient.listTrackedLink(request))
                .thenReturn(Mono.just(emptyList));

        // when
        SendMessage sendMessage = commandList.handle(update);
        Map<String, Object> result = sendMessage.getParameters();

        // then
        assertAll(
                () -> assertNull(result.get("parse_mode"))
        );
    }
}
