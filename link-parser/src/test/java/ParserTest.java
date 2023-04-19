import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.dto.UrlData;
import ru.tinkoff.edu.java.parser.GitHubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;


public class ParserTest {
    @Test
    public void testGitHubParserWithValidLink() {
        String url = "https://github.com/Fobiak/Fobiak_tinkoff";
        String expected = "GitHubData[username=Fobiak, repository=Fobiak_tinkoff]";
        Parser gitHubParser = new GitHubParser();
        String result = String.valueOf(gitHubParser.parse(url));
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testStackOverflowParserWithValidLink() {
        String url = "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c";
        String expected = "StackOverflowData[id=1642028]";
        Parser stackOverflowParser = new StackOverflowParser();
        String result = String.valueOf(stackOverflowParser.parse(url));
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testStackOverflowParserWithInvalidLink() {
        String url = "https://ficko.com/questions/1642028/what-is-the-operator-in-c";
        Parser stackOverflowParser = new StackOverflowParser();
        UrlData result = stackOverflowParser.parse(url);
        Assertions.assertNull(result);
    }

    @Test
    public void testGitHubParserWithInvalidLink() {
        String url = "https://ficko.com/Fobiak/Fobiak_tinkoff";
        Parser gitHubParser = new GitHubParser();
        UrlData result = gitHubParser.parse(url);
        Assertions.assertNull(result);
    }

    @Test
    public void testChainOfResponsibilityWithValidLink() {
        String url = "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c";
        String expected = "StackOverflowData[id=1642028]";
        Parser parser = Parser.createParserChain();
        String result = String.valueOf(parser.parse(url));
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testChainOfResponsibilityWithInvalidLink() {
        String url = "https://tcsbank.ru/some-secrets";
        Parser parser = Parser.createParserChain();
        UrlData result = parser.parse(url);
        Assertions.assertNull(result);
    }
}
