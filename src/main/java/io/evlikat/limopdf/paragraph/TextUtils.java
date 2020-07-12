package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.paragraph.hyphenation.NoHyphenation;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static List<String> splitByWords(String string) {
        return splitByWords(string, NoHyphenation.INSTANCE);
    }

    public static List<String> splitByWords(String string, HyphenationRules hyphenationRules) {
        StringBuilder sb = new StringBuilder();
        List<String> result = new ArrayList<>();
        boolean lastDelimiter = false;
        for (char c : string.toCharArray()) {
            if (hyphenationRules.isDelimiter(c)) {
                sb.append(c);
                lastDelimiter = true;
            } else {
                if (lastDelimiter) {
                    result.add(sb.toString());
                    sb = new StringBuilder();

                    sb.append(c);

                    lastDelimiter = false;
                } else {
                    sb.append(c);
                }
            }
        }
        if (sb.length() > 0) {
            result.add(sb.toString());
        }
        return result;
    }

    private TextUtils() {
    }
}
