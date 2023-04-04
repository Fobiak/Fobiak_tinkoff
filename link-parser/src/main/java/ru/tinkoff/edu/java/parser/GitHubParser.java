package ru.tinkoff.edu.java.parser;



import ru.tinkoff.edu.java.dto.GitHubData;
import ru.tinkoff.edu.java.dto.UrlData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GitHubParser extends AbstractParser {
    @Override
    public UrlData parse(String url) {
        if (url.startsWith("https://github.com")) {
            String patternString = "^https?://github\\.com/([\\w-]+)/([\\w-]+)";
            Matcher matcher = Pattern.compile(patternString).matcher(url);
            return matcher.find() ? new GitHubData(matcher.group(1), matcher.group(2)) : null;
        }
        return nextParser == null ? null : nextParser.parse(url);
    }
}