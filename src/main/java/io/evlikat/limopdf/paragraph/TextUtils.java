package io.evlikat.limopdf.paragraph;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static List<String> splitByWords(String string) {
        StringBuilder sb = new StringBuilder();
        List<String> result = new ArrayList<>();
        boolean lastWhiteSpace = false;
        for (char c : string.toCharArray()) {
            if (Character.isWhitespace(c)) {
                sb.append(c);
                lastWhiteSpace = true;
            } else {
                if (lastWhiteSpace) {
                    result.add(sb.toString());
                    sb = new StringBuilder();

                    sb.append(c);

                    lastWhiteSpace = false;
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
