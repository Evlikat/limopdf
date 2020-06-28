package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.DrawResult;
import io.evlikat.limopdf.StickyDrawer;
import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.draw.DrawableTextLine;
import io.evlikat.limopdf.draw.TextLine;
import io.evlikat.limopdf.structure.Drawable;
import io.evlikat.limopdf.util.Box;
import io.evlikat.limopdf.util.CompositeBlockElement;

import java.util.Collection;
import java.util.List;

// TODO: this is not only a paragraph drawer: decompose
public class PdfParagraphDrawer implements StickyDrawer {

    /**
     * Contains partial paragraph lines ready to draw.
     */
    private final ParagraphIterator paragraphIterator;

    public PdfParagraphDrawer(PdfParagraph paragraph) {
        this.paragraphIterator = new ParagraphIterator(List.of(paragraph));
    }

    @Override
    public void addFollowingDrawables(Collection<Drawable> drawables) {
        drawables.forEach(d -> paragraphIterator.addFollowingParagraph((PdfParagraph) d));
    }

    @Override
    public DrawResult draw(DrawableArea drawableArea) {
        if (paragraphIterator.isComplete()) {
            return DrawResult.COMPLETE;
        }
        float availableWidth = drawableArea.getAvailableWidth();
        TextLineGroupIterator lineGroupIterator = paragraphIterator.getTextLineGroupIteratorFor(availableWidth);
        return doDraw(drawableArea, lineGroupIterator, availableWidth);
    }

    private DrawResult doDraw(DrawableArea drawableArea, TextLineGroupIterator lineGroupIterator, float availableWidth) {
        boolean anyLineDrawn = false;
        while (lineGroupIterator.hasNext()) {
            TextLineGroup lineGroup = lineGroupIterator.peek();

            CompositeBlockElement composite = buildCompositeBlockElement(lineGroup);

            if (drawableArea.canDraw(composite)) {
                anyLineDrawn = true;

                for (int lineNum = 0; lineNum < lineGroup.size(); lineNum++) {
                    TextLine line = lineGroup.get(lineNum);
                    float leftIndent = chooseLeftIndent(availableWidth, line);
                    float bottomMargin = line.isLast() ? line.getParagraphMargin().getBottom() : 0f;
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
                    lineGroupIterator.ignoreKeepTogether();
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
        CompositeBlockElement composite = new CompositeBlockElement();

        for (TextLine line : lineGroup) {
            float topMargin = line.isFirst() ? line.getParagraphMargin().getTop() : 0f;
            float topPadding = line.isFirst() ? 0f : line.getLineSpacingInPixels();
            composite.addBlockElement(line, topPadding, topMargin);
        }

        return composite;
    }

    private float chooseLeftIndent(float availableWidth,
                                   TextLine textLine) {
        Box margin = textLine.getParagraphMargin();
        float remainingContentWidth = availableWidth - margin.getLeft() - margin.getRight();
        HorizontalTextAlignment horizontalTextAlignment = textLine.getHorizontalTextAlignment();
        switch (horizontalTextAlignment) {
            case LEFT:
                return margin.getLeft();
            case CENTER:
                return margin.getLeft() + (remainingContentWidth - textLine.getWidth()) / 2;
            case RIGHT:
                return margin.getLeft() + remainingContentWidth - textLine.getWidth();
        }
        throw new UnsupportedOperationException("Unsupported alignment: " + horizontalTextAlignment);
    }
}
