package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.Drawer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class PdfParagraph implements Drawable {

    private final List<PdfParagraphChunk> chunks = new ArrayList<>();

    public PdfParagraph(PdfParagraphChunk chunk) {
        this.chunks.add(chunk);
    }

    public PdfParagraph(String text) {
        this(new PdfParagraphChunk(text, new PdfCharacterProperties()));
    }

    @Override
    public Drawer drawer() {
        return new PdfParagraphDrawer(this);
    }
}
