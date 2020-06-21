package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.draw.TextLine;

import java.util.List;

public class TextLineGroupIterator {

    private final List<TextLine> source;

    private boolean keepTogether;
    private final int maxOrphanLinesAllowed;
    private int currentIndex = 0;

    public TextLineGroupIterator(List<TextLine> source, PdfParagraphProperties paragraphProperties) {
        this.source = source;

        this.keepTogether = paragraphProperties.isKeepTogether();
        this.maxOrphanLinesAllowed = paragraphProperties.getMaxOrphanLinesAllowed();
    }

    public boolean hasNext() {
        return currentIndex < source.size();
    }

    public TextLineGroup peek() {
        if (keepTogether) {
            return new TextLineGroup(source.subList(currentIndex, source.size()));
        } else if (maxOrphanLinesAllowed > 1) {
            if (isEdge()) {
                return new TextLineGroup(source.subList(currentIndex, Math.min(source.size(), currentIndex + maxOrphanLinesAllowed)));
            } else {
                return new TextLineGroup(source.get(currentIndex));
            }
        }
        return new TextLineGroup(source.get(currentIndex));
    }

    public void confirm(TextLineGroup group) {
        currentIndex += group.size();
    }

    public void dropKeepTogether() {
        keepTogether = false;
    }

    private boolean isEdge() {
        return currentIndex == 0 || currentIndex >= source.size() - maxOrphanLinesAllowed;
    }
}
