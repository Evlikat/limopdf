package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.table.PdfRow;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DrawableRow {

    @Getter
    private final List<DrawableCell> cells;
    @Getter
    private final PdfRow sourceRow;

    public DrawableRow(PdfRow sourceRow, List<ColumnDefinition> columnDefinitions) {
        this.sourceRow = sourceRow;
        this.cells = sourceRow.getCells().stream()
            .map(sourceCell -> new DrawableCell(sourceCell))
            .collect(toList());
    }

    public float getHeight() {
        return (float) cells.stream().mapToDouble(DrawableCell::getHeight).max().orElse(0);
    }
}
