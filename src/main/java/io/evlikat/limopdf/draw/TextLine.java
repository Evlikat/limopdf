package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.paragraph.HorizontalTextAlignment;
import io.evlikat.limopdf.paragraph.PdfParagraph;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.IBlock;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class TextLine implements IBlock {

    @Getter
    private float height = 0f;
    @Getter
    private final List<TextChunk> chunks = new ArrayList<>();
    @Setter
    @Getter
    private float lineIndent = 0f;
    @Setter
    @Getter
    private boolean first = false;
    @Setter
    @Getter
    private boolean last = false;
    @Setter
    private PdfParagraph paragraph;

    public TextLine() {
    }

    public TextLine(TextChunk chunk) {
        chunks.add(chunk);
    }

    public TextLine(List<TextChunk> chunks) {
        chunks.forEach(this::addChunk);
    }

    @SneakyThrows
    public void addChunk(TextChunk chunk) {
        this.height = Math.max(height, chunk.getCharacterProperties().getHeightInPixels());
        this.chunks.add(chunk);
    }

    public boolean anyChunk() {
        return !this.chunks.isEmpty();
    }

    public HorizontalTextAlignment getHorizontalTextAlignment() {
        return paragraph.getParagraphProperties().getHorizontalTextAlignment();
    }

    public Box getParagraphMargin() {
        return paragraph.getParagraphProperties().getMargin();
    }

    public float getLineSpacingInPixels() {
        return paragraph.getParagraphProperties().getLineSpacingInPixels();
    }

    public boolean isKeepWithNext() {
        return paragraph.getParagraphProperties().isKeepWithNext();
    }

    public boolean isKeepTogether() {
        return paragraph.getParagraphProperties().isKeepTogether();
    }

    public int getMaxOrphanLinesAllowed() {
        return paragraph.getParagraphProperties().getMaxOrphanLinesAllowed();
    }

    @Override
    public String toString() {
        return "TextLine{" +
            "chunks=" + chunks +
            '}';
    }
}
