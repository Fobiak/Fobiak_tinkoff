import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tinkoff.edu.java.bot.client.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.client.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.client.impl.ScrapperClientImpl;
import ru.tinkoff.edu.java.bot.telegram.command.ListCommand;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {
    @Mock
    private ScrapperClientImpl client;
    private ListCommand listCommand;

    @BeforeEach
    public void setUp() {
        listCommand = new ListCommand();
        listCommand.setClient(client);
    }

    @Test
    public void when_ListLinksIsEmpty_Expect_SpecialMessage() {
        when(client.getAllLinks(anyLong())).thenReturn(getListLinksResponse(0));
        SendMessage response = listCommand.handle(getUpdate());
        assertEquals("Нет отслеживаемых ссылок", response.getText());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    public void when_ListLinksIsNotEmpty_Expect_ListLinks(int size) {
        when(client.getAllLinks(anyLong())).thenReturn(getListLinksResponse(size));
        SendMessage response = listCommand.handle(getUpdate());
        Assertions.assertAll(
                () -> assertTrue(response.getText().startsWith("Список отслеживаемых ссылок:")),
                () -> assertEquals(size, response.getText().split("\n").length - 1)
        );
    }

    private Update getUpdate() {
        Update update = new Update();
        Message msg = new Message();
        msg.setChat(new Chat(0L, "secret"));
        update.setMessage(msg);
        return update;
    }

    private ListLinksResponse getListLinksResponse(int size) {
        return new ListLinksResponse(
                Collections.nCopies(size, new LinkResponse(0, "tcsbank.ru/secrets")),
                size);
    }
}
