package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.paragraph.PdfCharacterProperties;
import lombok.Data;

@Data
public class TextChunk {

    private final String text;
    private final PdfCharacterProperties characterProperties;
    private final float totalWidth;
    /**
     * Chunk can contain end spaces. strippedWidth is a width ignoring end spaces.
     */
    private final float strippedWidth;

    @Override
    public String toString() {
        return text;
    }
}
