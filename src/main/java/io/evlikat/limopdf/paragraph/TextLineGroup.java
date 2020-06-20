package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.draw.TextLine;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

interface TextLines {
}

@Data
@AllArgsConstructor
class TextLineGroup implements TextLines, Iterable<TextLine> {
    private final List<TextLine> textLines;

    TextLineGroup(TextLine textLine) {
        this.textLines = Collections.singletonList(textLine);
    }

    int size() {
        return textLines.size();
    }

    TextLine get(int index) {
        return textLines.get(index);
    }

    @Override
    public Iterator<TextLine> iterator() {
        return textLines.iterator();
    }
}