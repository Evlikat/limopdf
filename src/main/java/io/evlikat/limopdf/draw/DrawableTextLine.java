package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.paragraph.HorizontalTextAlignment;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.IBlockElement;
import io.evlikat.limopdf.util.IRectangle;
import io.evlikat.limopdf.util.Rule;
import lombok.Data;
import lombok.Getter;
import one.util.streamex.EntryStream;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Data
public class DrawableTextLine implements IRectangle, IBlockElement {

    @Getter(PRIVATE)
    private final TextLine line;
    private final float leftIndent;
    private final float topPadding;
    private final float topMargin;
    private final float bottomMargin;

    private final float width;

    private final List<DrawableTextChunk> chunks;

    public DrawableTextLine(TextLine line,
                            float availableWidth,
                            float topPadding,
                            float topMargin,
                            float bottomMargin) {
        this.line = line;
        this.topPadding = topPadding;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;

        this.chunks = getDrawableChunks(line);
        this.width = calculateWidth();
        this.leftIndent = chooseLeftIndent(availableWidth, line, this.width);
    }

    @Override
    public float getHeight() {
        return line.getHeight();
    }

    @Override
    public float getWidth() {
        return width;
    }

    private List<DrawableTextChunk> getDrawableChunks(TextLine line) {
        List<TextChunk> chunks = line.getChunks();
        return EntryStream.of(chunks)
            .mapKeyValue((i, chunk) -> new DrawableTextChunk(chunk, i == chunks.size() - 1))
            .collect(Collectors.toList());
    }

    @Rule
    private float calculateWidth() {
        return (float) this.chunks.stream().mapToDouble(DrawableTextChunk::getWidth).sum();
    }

    @Rule
    private float chooseLeftIndent(
        float availableWidth,
        TextLine textLine,
        float drawableTextLineWidth
    ) {
        Box margin = textLine.getParagraphMargin();
        float remainingContentWidth = availableWidth - margin.getLeft() - margin.getRight();
        HorizontalTextAlignment horizontalTextAlignment = textLine.getHorizontalTextAlignment();
        switch (horizontalTextAlignment) {
            case LEFT:
                return margin.getLeft();
            case CENTER:
                return margin.getLeft() + (remainingContentWidth - drawableTextLineWidth) / 2;
            case RIGHT:
                return margin.getLeft() + remainingContentWidth - drawableTextLineWidth;
        }
        throw new UnsupportedOperationException("Unsupported alignment: " + horizontalTextAlignment);
    }
}
