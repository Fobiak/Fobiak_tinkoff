package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.dto.UrlData;

public sealed interface Parser permits AbstractParser {
    UrlData parse(String url);

    void setNextParser(Parser nextParser);

    static Parser createParserChain() {
        Parser gitHubParser = new GitHubParser();
        Parser stackOverflowParser = new StackOverflowParser();
        gitHubParser.setNextParser(stackOverflowParser);
        return gitHubParser;
    }
}
