package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.CurrentPositionHolder;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class DrawableArea {

    private final PDPageContentStream contentStream;
    private final CurrentPositionHolder position;
    private final float availableWidth;
    private float availableHeight;

    public DrawableArea(PDPageContentStream contentStream, CurrentPositionHolder position, float availableWidth, float availableHeight) {
        this.contentStream = contentStream;
        this.position = position;
        this.availableWidth = availableWidth;
        this.availableHeight = availableHeight;
    }

    public float getAvailableWidth() {
        return availableWidth;
    }

    @SneakyThrows
    public void drawTextLine(TextLine line) {
        contentStream.beginText();

        for (TextChunk textChunk : line.getChunks()) {
            textChunk.setUpContentStream(contentStream);
            contentStream.newLineAtOffset(position.getX(), position.getY() - line.getHeight());
            contentStream.showText(textChunk.getText());

            position.minusY(line.getHeight());
            availableHeight -= line.getHeight();
        }

        contentStream.endText();
    }
}
