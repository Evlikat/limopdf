package io.evlikat.limopdf.util.grid;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GridCell<C extends ICell> {

    private int row;
    private int column;
    private boolean rowSpanStart;
    private boolean rowSpanEnd;
    private boolean colSpanStart;
    private boolean colSpanEnd;
    private C cell;

    public boolean isInSpan() {
        return !rowSpanStart || !colSpanStart;
    }
}
