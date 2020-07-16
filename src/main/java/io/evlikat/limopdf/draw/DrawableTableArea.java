package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.CurrentPosition;
import io.evlikat.limopdf.util.grid.GridCell;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class DrawableTableArea implements DrawableContentArea {

    private final PDPageContentStream contentStream;
    private final CurrentPosition position;
    private final DrawableTable drawableTable;
    @Getter
    private final float totalBlockHeight;
    private final float topMargin;

    public DrawableTableArea(PDPageContentStream contentStream,
                             CurrentPosition position,
                             DrawableTable drawableTable) {
        this.contentStream = contentStream;
        this.position = position;
        this.drawableTable = drawableTable;

        this.topMargin = Math.max(position.getLastBottomMargin(), drawableTable.getTopMargin());
        this.totalBlockHeight = drawableTable.getHeight() + topMargin + drawableTable.getTopPadding();
    }

    @Override
    @SneakyThrows
    public void fill() {

        float tx = position.getX();
        float ty = position.getY() - topMargin - drawableTable.getTopPadding();

        int gridWidth = drawableTable.getGridWidth();
        int gridHeight = drawableTable.getGridHeight();

        float yEffective = ty;

        for (int gridRow = 0; gridRow < gridHeight; gridRow++) {

            float xEffective = tx;

            for (int gridColumn = 0; gridColumn < gridWidth; gridColumn++) {
                GridCell<DrawableCell> gridCell = drawableTable.getCellAt(gridColumn, gridRow);

                float cellWidth = gridCell.getCell().getWidth();
                float cellHeight = gridCell.getCell().getHeight();
                // left
                if (gridCell.isColSpanStart()) {
                    contentStream.moveTo(xEffective, yEffective);
                    contentStream.lineTo(xEffective, yEffective - cellHeight);
                    contentStream.stroke();
                }
                // right
                if (gridCell.isColSpanEnd()) {
                    contentStream.moveTo(xEffective + cellWidth, yEffective);
                    contentStream.lineTo(xEffective + cellWidth, yEffective - cellHeight);
                    contentStream.stroke();
                }
                // top
                if (gridCell.isRowSpanStart()) {
                    contentStream.moveTo(xEffective, yEffective);
                    contentStream.lineTo(xEffective + cellWidth, yEffective);
                    contentStream.stroke();
                }
                // bottom
                if (gridCell.isRowSpanEnd()) {
                    contentStream.moveTo(xEffective, yEffective - cellHeight);
                    contentStream.lineTo(xEffective + cellWidth, yEffective - cellHeight);
                    contentStream.stroke();
                }

                xEffective += cellWidth;
            }

            yEffective -= drawableTable.getRowHeight(gridRow);
        }
    }

    @Override
    public float getAreaWidth() {
        return drawableTable.getWidth();
    }
}
