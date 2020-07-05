package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.DrawResult;
import io.evlikat.limopdf.StickyDrawer;
import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.draw.DrawableTextLine;
import io.evlikat.limopdf.draw.TextLine;
import io.evlikat.limopdf.structure.Drawable;
import io.evlikat.limopdf.util.CompositeBlockElement;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

            List<DrawableTextLine> drawableTextLines = prepareLineToDraw(lineGroup.getTextLines(), availableWidth);

            CompositeBlockElement composite = new CompositeBlockElement(drawableTextLines);

            if (drawableArea.canDraw(composite)) {
                anyLineDrawn = true;

                for (DrawableTextLine drawableTextLine : drawableTextLines) {
                    drawableArea.drawTextLine(drawableTextLine);
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

    private List<DrawableTextLine> prepareLineToDraw(List<TextLine> lines, float availableWidth) {
        return lines.stream().map(line -> {
            float topMargin = line.isFirst() ? line.getParagraphMargin().getTop() : 0f;
            float topPadding = line.isFirst() ? 0f : line.getLineSpacingInPixels();
            float bottomMargin = line.isLast() ? line.getParagraphMargin().getBottom() : 0f;
            return new DrawableTextLine(line,
                availableWidth,
                line.getLineIndent(),
                topPadding,
                topMargin,
                bottomMargin);
        }).collect(Collectors.toList());
    }
}
