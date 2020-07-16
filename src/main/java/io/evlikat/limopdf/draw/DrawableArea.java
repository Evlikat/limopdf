package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.CurrentPositionHolder;
import io.evlikat.limopdf.util.IBlockElement;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

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

    public boolean canDraw(IBlockElement blockElement) {
        float margin = Math.max(position.getLastBottomMargin(), blockElement.getTopMargin());
        return availableHeight - blockElement.getTopPadding() >= (margin + blockElement.getHeight());
    }

    public void drawTable(DrawableTable drawableTable) {
        DrawableTableArea area = new DrawableTableArea(contentStream, position, drawableTable);
        area.fill();
    }

    @SneakyThrows
    public void drawTextLine(DrawableTextLine drawableTextLine) {
        DrawableTextLineArea area = new DrawableTextLineArea(contentStream, position, drawableTextLine);
        area.fill();

        position.minusY(area.getTotalBlockHeight());
        position.setLastBottomMargin(drawableTextLine.getBottomMargin());
        availableHeight -= area.getTotalBlockHeight();
        position.setBlank(false);
    }

    public boolean isBlank() {
        return position.isBlank();
    }
}
