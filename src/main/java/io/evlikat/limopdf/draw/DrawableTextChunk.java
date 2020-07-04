package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.paragraph.PdfCharacterProperties;
import lombok.Data;

@Data
public class DrawableTextChunk {

    private final TextChunk textChunk;
    private final boolean isLastInLine;

    public String getText() {
        return textChunk.getText();
    }

    public PdfCharacterProperties getCharacterProperties() {
        return textChunk.getCharacterProperties();
    }

    public float getWidth() {
        return isLastInLine ? textChunk.getStrippedWidth() : textChunk.getTotalWidth();
    }
}
