package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.CurrentPositionHolder;
import io.evlikat.limopdf.util.IBlock;
import io.evlikat.limopdf.util.devtools.PrintMode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

public class DrawableArea {

    private final PDPageContentStream contentStream;
    private final CurrentPositionHolder position;
    @Getter
    private final float availableWidth;
    @Getter
    private float availableHeight;

    public DrawableArea(PDPageContentStream contentStream, CurrentPositionHolder position, float availableWidth, float availableHeight) {
        this.contentStream = contentStream;
        this.position = position;
        this.availableWidth = availableWidth;
        this.availableHeight = availableHeight;
    }

    public boolean canDraw(IBlock block, float topMargin) {
        float margin = Math.max(position.getLastBottomMargin(), topMargin);
        return availableHeight >= (margin + block.getHeight());
    }

    @SneakyThrows
    public void drawTextLine(DrawableTextLine drawableTextLine) {

        TextLine line = drawableTextLine.getLine();

        float topMargin = Math.max(position.getLastBottomMargin(), drawableTextLine.getTopMargin());

        float tx = position.getX() + drawableTextLine.getLeftIndent();
        float ty = position.getY() - (line.getHeight() + topMargin);

        drawDebugLines(line, tx, ty);

        contentStream.beginText();

        for (TextChunk textChunk : line.getChunks()) {
            textChunk.setUpContentStream(contentStream);
            contentStream.newLineAtOffset(tx, ty);
            contentStream.showText(textChunk.getText());
        }

        position.minusY(line.getHeight() + topMargin);
        position.setLastBottomMargin(drawableTextLine.getBottomMargin());
        availableHeight -= line.getHeight();

        contentStream.endText();
    }

    private void drawDebugLines(TextLine line, float tx, float ty) throws IOException {
        if (!PrintMode.INSTANCE.isDebug()) {
            return;
        }
        contentStream.setStrokingColor(Color.LIGHT_GRAY);
        contentStream.setLineWidth(1f);
        contentStream.addRect(tx, ty, line.getWidth(), line.getHeight());
        contentStream.stroke();
    }
}
