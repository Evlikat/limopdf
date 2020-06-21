package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.DrawResult;
import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.draw.DrawableTextLine;
import io.evlikat.limopdf.draw.TextLine;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.CompositeBlockElement;

import java.util.List;

public class PdfParagraphDrawer implements Drawer {

    private final PdfParagraph paragraph;
    /**
     * Contains partial paragraph lines ready to draw.
     */
    private TextLineGroupIterator lineGroupIterator;

    public PdfParagraphDrawer(PdfParagraph paragraph) {
        this.paragraph = paragraph;
    }

    @Override
    public DrawResult draw(DrawableArea drawableArea) {
        if (lineGroupIterator != null && !lineGroupIterator.hasNext()) {
            return DrawResult.COMPLETE;
        }
        float availableWidth = drawableArea.getAvailableWidth();
        TextLineGroupIterator lineGroupIterator = prepareOrRestoreTextLines(availableWidth);
        return doDraw(drawableArea, lineGroupIterator, availableWidth);
    }

    private TextLineGroupIterator prepareOrRestoreTextLines(float availableWidth) {
        if (lineGroupIterator == null) {
            Box margin = paragraph.getParagraphProperties().getMargin();
            float remainingContentWidth = availableWidth - margin.getLeft() - margin.getRight();

            List<TextLine> textLines = PdfParagraphDrawerUtils.wrapLines(paragraph.getChunks(), remainingContentWidth);
            if (!textLines.isEmpty()) {
                textLines.get(0).setFirst(true);
                textLines.get(textLines.size() - 1).setLast(true);
            }
            lineGroupIterator = new TextLineGroupIterator(textLines, paragraph.getParagraphProperties());
        }
        return lineGroupIterator;
    }

    private DrawResult doDraw(DrawableArea drawableArea, TextLineGroupIterator lineGroupIterator, float availableWidth) {
        PdfParagraphProperties paragraphProperties = paragraph.getParagraphProperties();
        Box margin = paragraphProperties.getMargin();

        boolean anyLineDrawn = false;
        while (lineGroupIterator.hasNext()) {
            TextLineGroup lineGroup = lineGroupIterator.peek();

            CompositeBlockElement composite = buildCompositeBlockElement(lineGroup);

            if (drawableArea.canDraw(composite)) {
                anyLineDrawn = true;

                for (int lineNum = 0; lineNum < lineGroup.size(); lineNum++) {
                    TextLine line = lineGroup.get(lineNum);
                    float leftIndent = chooseLeftIndent(availableWidth, line.getWidth(), paragraphProperties);
                    float bottomMargin = line.isLast() ? margin.getBottom() : 0f;
                    drawableArea.drawTextLine(
                        new DrawableTextLine(line,
                            leftIndent,
                            composite.getTopPadding(lineNum),
                            composite.getTopMargin(lineNum),
                            bottomMargin)
                    );
                }

                lineGroupIterator.confirm(lineGroup);
            } else {
                if (drawableArea.isBlank()) {
                    lineGroupIterator.dropKeepTogether();
                    continue;
                }
                if (anyLineDrawn) {
                    return DrawResult.PARTIAL;
                } else {
                    return DrawResult.REJECTED;
                }
            }
        }
        return DrawResult.COMPLETE;
    }

    private CompositeBlockElement buildCompositeBlockElement(TextLineGroup lineGroup) {
        PdfParagraphProperties paragraphProperties = paragraph.getParagraphProperties();
        Box margin = paragraphProperties.getMargin();

        CompositeBlockElement composite = new CompositeBlockElement();

        for (TextLine line : lineGroup) {
            float topMargin = line.isFirst() ? margin.getTop() : 0f;
            float topPadding = line.isFirst() ? 0f : paragraphProperties.getLineSpacingInPixels();
            composite.addBlockElement(line, topPadding, topMargin);
        }

        return composite;
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
