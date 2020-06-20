package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.DrawResult;
import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.draw.DrawableTextLine;
import io.evlikat.limopdf.draw.TextLine;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.CompositeBlockElement;

import java.util.List;

import static io.evlikat.limopdf.util.CollectionUtils.groupEdges;
import static java.util.stream.Collectors.toList;

public class PdfParagraphDrawer implements Drawer {

    private final PdfParagraph paragraph;
    /**
     * Contains partial paragraph lines ready to draw.
     */
    private List<TextLineGroup> remainingTextLineGroups;
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
        List<TextLineGroup> lines = prepareOrRestoreTextLines(availableWidth);
        return doDraw(drawableArea, lines, availableWidth);
    }

    private List<TextLineGroup> prepareOrRestoreTextLines(float availableWidth) {
        if (remainingTextLineGroups != null) {
            List<TextLineGroup> lines = remainingTextLineGroups;
            remainingTextLineGroups = null;
            return lines;
        }

        Box margin = paragraph.getParagraphProperties().getMargin();
        float remainingContentWidth = availableWidth - margin.getLeft() - margin.getRight();

        List<TextLine> textLines = PdfParagraphDrawerUtils.wrapLines(paragraph.getChunks(), remainingContentWidth);
        if (!textLines.isEmpty()) {
            textLines.get(0).setFirst(true);
            textLines.get(textLines.size() - 1).setLast(true);
        }
        return groupLines(textLines);
    }

    private DrawResult doDraw(DrawableArea drawableArea, List<TextLineGroup> lineGroups, float availableWidth) {
        PdfParagraphProperties paragraphProperties = paragraph.getParagraphProperties();
        Box margin = paragraphProperties.getMargin();

        boolean anyLineDrawn = false;
        for (int lineGroupNum = 0; lineGroupNum < lineGroups.size(); lineGroupNum++) {
            TextLineGroup lineGroup = lineGroups.get(lineGroupNum);

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
            } else {
                remainingTextLineGroups = lineGroups.subList(lineGroupNum, lineGroups.size());
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

    private List<TextLineGroup> groupLines(List<TextLine> textLines) {
        if (paragraph.getParagraphProperties().isOrphanControl()) {
            return groupEdges(textLines, 2).stream().map(TextLineGroup::new).collect(toList());
        } else {
            return textLines.stream().map(TextLineGroup::new).collect(toList());
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
