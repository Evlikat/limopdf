package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.DrawResult;
import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.draw.DrawableTextLine;
import io.evlikat.limopdf.draw.TextLine;
import io.evlikat.limopdf.util.Box;

import java.util.List;

public class PdfParagraphDrawer implements Drawer {

    private final PdfParagraph paragraph;
    /**
     * Contains partial paragraph lines ready to draw.
     */
    private List<TextLine> remainingTextLines;
    /**
     * Indicates that object was drawn completely.
     */
    private boolean isComplete = false;

    public PdfParagraphDrawer(PdfParagraph paragraph) {
        this.paragraph = paragraph;
    }

    @Override
    public DrawResult draw(DrawableArea drawableArea) {
        if (isComplete) {
            return DrawResult.COMPLETE;
        }
        float availableWidth = drawableArea.getAvailableWidth();
        List<TextLine> lines = prepareOrRestoreTextLines(availableWidth);
        return doDraw(drawableArea, lines, availableWidth);
    }

    private List<TextLine> prepareOrRestoreTextLines(float availableWidth) {
        if (remainingTextLines != null) {
            List<TextLine> lines = remainingTextLines;
            remainingTextLines = null;
            return lines;
        }

        Box margin = paragraph.getParagraphProperties().getMargin();
        float remainingContentWidth = availableWidth - margin.getLeft() - margin.getRight();

        List<TextLine> textLines = PdfParagraphDrawerUtils.wrapLines(paragraph.getChunks(), remainingContentWidth);
        if (!textLines.isEmpty()) {
            textLines.get(0).setFirst(true);
            textLines.get(textLines.size() - 1).setLast(true);
        }
        return textLines;
    }

    private DrawResult doDraw(DrawableArea drawableArea, List<TextLine> lines, float availableWidth) {
        PdfParagraphProperties paragraphProperties = paragraph.getParagraphProperties();
        Box margin = paragraphProperties.getMargin();
        boolean anyLineDrawn = false;
        for (int i = 0; i < lines.size(); i++) {
            TextLine line = lines.get(i);

            float topMargin = line.isFirst() ? margin.getTop() : 0f;
            float topPadding = line.isFirst() ? 0f : paragraphProperties.getLineSpacingInPixels();
            if (drawableArea.canDraw(line, topPadding, topMargin)) {
                anyLineDrawn = true;

                float leftIndent = chooseLeftIndent(availableWidth, line.getWidth(), paragraphProperties);
                float bottomMargin = line.isLast() ? margin.getBottom() : 0f;
                drawableArea.drawTextLine(new DrawableTextLine(line, leftIndent, topPadding, topMargin, bottomMargin));
            } else {
                remainingTextLines = lines.subList(i, lines.size());
                if (anyLineDrawn) {
                    return DrawResult.PARTIAL;
                } else {
                    return DrawResult.REJECTED;
                }
            }
        }
        isComplete = true;
        return DrawResult.COMPLETE;
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
