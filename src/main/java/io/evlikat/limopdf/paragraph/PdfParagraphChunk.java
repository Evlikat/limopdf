package io.evlikat.limopdf.paragraph;

import lombok.Getter;

import java.util.Objects;

@Getter
public class PdfParagraphChunk {

    private final String text;
    private final PdfCharacterProperties properties;

    public PdfParagraphChunk(String text, PdfCharacterProperties properties) {
        Objects.requireNonNull(text);
        this.text = text;
        this.properties = properties;
    }

    @Override
    public String toString() {
        return text;
    }
}
