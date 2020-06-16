package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.Drawer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PdfParagraph implements Drawable {

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
    public Drawer drawer() {
        return new PdfParagraphDrawer(this);
    }
}
