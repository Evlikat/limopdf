package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.draw.TextLine;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Data
@AllArgsConstructor
class TextLineGroup implements Iterable<TextLine> {

    private final List<TextLine> textLines;
    private final int confirmGroupIndex;
    private final int confirmLineIndex;

    TextLineGroup(TextLine textLine, int confirmGroupIndex, int confirmLineIndex) {
        this.textLines = Collections.singletonList(textLine);
        this.confirmGroupIndex = confirmGroupIndex;
        this.confirmLineIndex = confirmLineIndex;
    }

    @Override
    public Iterator<TextLine> iterator() {
        return textLines.iterator();
    }
}