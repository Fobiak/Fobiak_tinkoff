package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.dto.StackOverflowData;
import ru.tinkoff.edu.java.dto.UrlData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StackOverflowParser extends AbstractParser {
    @Override
    public UrlData parse(String url) {
        if (url.startsWith("https://stackoverflow.com")) {
            String patternString = "^https?://stackoverflow\\.com/questions/(\\d+)/";
            Matcher matcher = Pattern.compile(patternString).matcher(url);
            return matcher.find() ? new StackOverflowData(Integer.parseInt(matcher.group(1))) : null;
        }
        return nextParser == null ? null : nextParser.parse(url);
    }
}
