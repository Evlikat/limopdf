package io.evlikat.limopdf.paragraph;

import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.draw.TextLine;

import java.util.List;

public class PdfParagraphDrawer implements Drawer {

    private final PdfParagraph paragraph;

    public PdfParagraphDrawer(PdfParagraph paragraph) {
        this.paragraph = paragraph;
    }

    @Override
    public void draw(DrawableArea drawableArea) {
        float remainingWidth = drawableArea.getAvailableWidth();

        List<TextLine> lines = PdfParagraphDrawerUtils.wrapLines(paragraph.getChunks(), remainingWidth);

        for (TextLine line : lines) {
            drawableArea.drawTextLine(line);
        }
    }
}
