package io.evlikat.limopdf.paragraph.hyphenation;

import io.evlikat.limopdf.paragraph.HyphenationRules;

public enum DefaultHyphenation implements HyphenationRules {
    INSTANCE;

    @Override
    public boolean isDelimiter(char c) {
        return Character.isWhitespace(c) || c == '-';
    }
}
