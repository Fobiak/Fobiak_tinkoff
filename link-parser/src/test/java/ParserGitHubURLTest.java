import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.app.enums.TypeClient;
import ru.tinkoff.app.parser.ParserGitHubURL;
import ru.tinkoff.app.parser.ParserURL;
import ru.tinkoff.app.url.UrlDataGitHub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ParserGitHubURLTest {

    private ParserURL parserGitHubURL = new ParserGitHubURL();
    private final String TYPE_URL = "github.com";

    private final TypeClient typeClient = TypeClient.STACKOVERFLOW;

    @ParameterizedTest
    @ValueSource(strings = {"random text", "https://github.com/", "https://github.com/person/",
            "https://github.com/person//", "https://github.com/person///", "github.com/person/rep/",
            "https://stackoverflow.com/questions/1642028/}"})
    void parseURL__transferredInvalidURL_URLDataGitHubIsNull(String invalidUrl) {
        assertNull(parserGitHubURL.parseUrl(invalidUrl));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "https://github.com/person/rep/////, person, rep",
            "https://github.com/person/rep/, person, rep",
            "https://github.com/o/r11//, o, r11",
    })
    void parseURL__transferredValidURL_URLDataGitHubIsCorrect(String validUrl, String userName, String rep) {

        assertEquals(parserGitHubURL.parseUrl(validUrl),
                new UrlDataGitHub(typeClient, userName, rep));
    }
}
