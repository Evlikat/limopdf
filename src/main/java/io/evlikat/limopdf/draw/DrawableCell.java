package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.table.PdfCell;
import io.evlikat.limopdf.util.grid.ICell;
import lombok.Getter;
import lombok.Setter;

public class DrawableCell implements ICell {

    private final PdfCell sourceCell;
    @Getter
    @Setter
    private float width;

    public DrawableCell(PdfCell sourceCell) {
        this.sourceCell = sourceCell;
    }

    @Override
    public float getHeight() {
//        sourceCell.getContent().get()
        return 50f;
    }

    @Override
    public int getRowSpan() {
        return sourceCell.getRowSpan();
    }

    @Override
    public int getColSpan() {
        return sourceCell.getColSpan();
    }
}
