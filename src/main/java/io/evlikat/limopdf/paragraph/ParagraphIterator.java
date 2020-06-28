package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.draw.TextLine;
import io.evlikat.limopdf.util.Box;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParagraphIterator {

    private final List<PdfParagraph> paragraphs;

    private TextLineGroupIterator lineGroupIterator;

    public ParagraphIterator(List<PdfParagraph> paragraphs) {
        this.paragraphs = new ArrayList<>(paragraphs);
    }

    public void addFollowingParagraph(PdfParagraph paragraph) {
        if (lineGroupIterator != null) {
            throw new IllegalStateException("The iterator started. " +
                "Add elements only before getTextLineGroupIteratorFor is called");
        }
        paragraphs.add(paragraph);
    }

    public boolean isComplete() {
        return lineGroupIterator != null && !lineGroupIterator.hasNext();
    }

    public TextLineGroupIterator getTextLineGroupIteratorFor(float availableWidth) {
        if (lineGroupIterator == null) {
            List<ParagraphTextLineGroup> allTextLineGroups = paragraphs.stream().map(paragraph -> {
                PdfParagraphProperties paragraphProperties = paragraph.getParagraphProperties();
                Box margin = paragraphProperties.getMargin();
                float remainingContentWidth = availableWidth - margin.getLeft() - margin.getRight();

                List<TextLine> textLines = PdfParagraphDrawerUtils.wrapLines(paragraph.getChunks(), remainingContentWidth);
                if (!textLines.isEmpty()) {
                    textLines.get(0).setFirst(true);
                    textLines.get(textLines.size() - 1).setLast(true);
                    textLines.forEach(textLine -> textLine.setParagraph(paragraph));
                }
                return new ParagraphTextLineGroup(paragraph.getParagraphProperties(), textLines);
            }).collect(Collectors.toList());

            lineGroupIterator = new TextLineGroupIterator(allTextLineGroups);
        }
        return lineGroupIterator;
    }
}
