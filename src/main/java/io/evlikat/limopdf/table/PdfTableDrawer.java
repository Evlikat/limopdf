package io.evlikat.limopdf.table;

import io.evlikat.limopdf.DrawResult;
import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.draw.DrawableArea;
import io.evlikat.limopdf.draw.DrawableTable;

public class PdfTableDrawer implements Drawer {

    private final PdfTable table;

    public PdfTableDrawer(PdfTable table) {
        this.table = table;
    }

    @Override
    public DrawResult draw(DrawableArea drawableArea) {
        DrawableTable drawableTable = new DrawableTable(table);

        if (drawableArea.canDraw(drawableTable)) {

            drawableArea.drawTable(drawableTable);
            return DrawResult.COMPLETE;
        } else {
            // split
            return DrawResult.PARTIAL;
        }
    }
}
