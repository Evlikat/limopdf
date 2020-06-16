package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.CurrentPositionHolder;
import io.evlikat.limopdf.util.devtools.PrintMode;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;

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
    public void drawTextLine(DrawableTextLine drawableTextLine) {

        TextLine line = drawableTextLine.getLine();

        float tx = position.getX() + drawableTextLine.getLeftIndent();
        float ty = position.getY() - line.getHeight();

        if (PrintMode.INSTANCE.isDebug()) {
            contentStream.setStrokingColor(Color.LIGHT_GRAY);
            contentStream.setLineWidth(1f);
            contentStream.addRect(tx, ty, line.getWidth(), line.getHeight());
            contentStream.stroke();
        }

        contentStream.beginText();

        for (TextChunk textChunk : line.getChunks()) {
            textChunk.setUpContentStream(contentStream);
            contentStream.newLineAtOffset(tx, ty);
            contentStream.showText(textChunk.getText());

            position.minusY(line.getHeight());
            availableHeight -= line.getHeight();
        }

        contentStream.endText();
    }
}
