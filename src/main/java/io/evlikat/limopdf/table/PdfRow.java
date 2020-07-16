package io.evlikat.limopdf.table;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PdfRow {

    @Getter
    private final List<PdfCell> cells = new ArrayList<>();

    public void addCell(PdfCell cell) {
        this.cells.add(cell);
    }

    public void addCells(List<PdfCell> cells) {
        this.cells.addAll(cells);
    }
}
