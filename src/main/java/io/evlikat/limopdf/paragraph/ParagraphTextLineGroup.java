package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.draw.TextLine;
import lombok.Data;

import java.util.List;

@Data
public class ParagraphTextLineGroup {

    private final PdfParagraphProperties paragraphProperties;
    private final List<TextLine> textLines;

    public int size() {
        return textLines.size();
    }
}
