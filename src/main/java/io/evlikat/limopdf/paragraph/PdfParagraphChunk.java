package io.evlikat.limopdf.paragraph;

import lombok.Getter;

import java.util.Objects;

@Getter
public class PdfParagraphChunk {

    private final String text;
    private final PdfCharacterProperties properties;
    private final HyphenationRules hyphenationRules;

    public PdfParagraphChunk(String text, PdfCharacterProperties properties) {
        this(text, properties, null);
    }

    public PdfParagraphChunk(String text, PdfCharacterProperties properties, HyphenationRules hyphenationRules) {
        Objects.requireNonNull(text);
        this.text = text;
        this.properties = properties;
        this.hyphenationRules = hyphenationRules;
    }

    @Override
    public String toString() {
        return text;
    }
}
