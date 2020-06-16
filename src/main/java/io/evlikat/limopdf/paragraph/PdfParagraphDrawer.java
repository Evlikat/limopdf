package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.draw.DrawableTextLine;
import io.evlikat.limopdf.draw.TextLine;
import io.evlikat.limopdf.util.Box;

import java.util.List;

public class PdfParagraphDrawer implements Drawer {

    private final PdfParagraph paragraph;

    public PdfParagraphDrawer(PdfParagraph paragraph) {
        this.paragraph = paragraph;
    }

    @Override
    public void draw(DrawableArea drawableArea) {
        float availableWidth = drawableArea.getAvailableWidth();

        Box margin = paragraph.getParagraphProperties().getMargin();
        float remainingContentWidth = availableWidth - margin.getLeft() - margin.getRight();

        List<TextLine> lines = PdfParagraphDrawerUtils.wrapLines(paragraph.getChunks(), remainingContentWidth);

        for (TextLine line : lines) {
            float leftIndent = chooseLeftIndent(availableWidth, line.getWidth(), paragraph.getParagraphProperties());
            drawableArea.drawTextLine(new DrawableTextLine(line, leftIndent));
        }
    }

    private float chooseLeftIndent(float availableWidth,
                                   float lineWidth,
                                   PdfParagraphProperties paragraphProperties) {
        Box margin = paragraph.getParagraphProperties().getMargin();
        float remainingContentWidth = availableWidth - margin.getLeft() - margin.getRight();
        HorizontalTextAlignment horizontalTextAlignment = paragraphProperties.getHorizontalTextAlignment();
        switch (horizontalTextAlignment) {
            case LEFT:
                return margin.getLeft();
            case CENTER:
                return margin.getLeft() + (remainingContentWidth - lineWidth) / 2;
            case RIGHT:
                return margin.getLeft() + remainingContentWidth - lineWidth;
        }
        throw new UnsupportedOperationException("Unsupported alignment: " + horizontalTextAlignment);
    }
}
