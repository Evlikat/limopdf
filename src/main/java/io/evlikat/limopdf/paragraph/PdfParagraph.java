package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.structure.Drawable;
import io.evlikat.limopdf.structure.StickyDrawable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PdfParagraph implements StickyDrawable, Drawable {

    private final PdfParagraphProperties paragraphProperties;
    private final List<PdfParagraphChunk> chunks = new ArrayList<>();

    public PdfParagraph(PdfParagraphChunk chunk, PdfParagraphProperties paragraphProperties) {
        this.paragraphProperties = paragraphProperties;
        this.chunks.add(chunk);
    }

    public PdfParagraph(String text, PdfParagraphProperties paragraphProperties) {
        this(new PdfParagraphChunk(text, new PdfCharacterProperties()), paragraphProperties);
    }

    public PdfParagraph(String text) {
        this(new PdfParagraphChunk(text, new PdfCharacterProperties()), new PdfParagraphProperties());
    }

    @Override
    public PdfParagraphDrawer drawer() {
        return new PdfParagraphDrawer(this);
    }

    @Override
    public boolean isKeepWithNext() {
        return paragraphProperties.isKeepWithNext();
    }
}
