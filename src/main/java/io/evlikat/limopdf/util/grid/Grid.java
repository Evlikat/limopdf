package io.evlikat.limopdf.util.grid;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.List;

public class Grid<C extends ICell> {

    private final Table<Integer, Integer, C> grid;

    public Grid(List<List<C>> rows) {
        this.grid = buildTable(rows);
    }

    private static <C extends ICell> Table<Integer, Integer, C> buildTable(List<List<C>> rows) {
        int gridHeight = rows.stream().map(row -> row.get(0)).mapToInt(ICell::getRowSpan).sum();
        int gridWidth = rows.get(0).stream().mapToInt(ICell::getColSpan).sum();
        HashBasedTable<Integer, Integer, C> grid = HashBasedTable.create(gridHeight, gridWidth);

        for (int gridRow = 0; gridRow < gridHeight; gridRow++) {
            for (int gridColumn = 0; gridColumn < gridWidth; gridColumn++) {
                // TODO: handle spans
                grid.put(gridRow, gridColumn, rows.get(gridRow).get(gridColumn));
            }
        }

        return grid;
    }

    public float getRowHeight(int gridRow) {
        return (float) grid.row(gridRow).values().stream().mapToDouble(ICell::getHeight).max().orElse(0);
    }

    public GridCell<C> getCellAt(int gridColumn, int gridRow) {
        // TODO: set inSpan
        return new GridCell<>(gridRow, gridColumn, true, true, true, true, grid.get(gridRow, gridColumn));
    }

    public int getGridHeight() {
        return grid.rowKeySet().size();
    }

    public int getGridWidth() {
        return grid.columnKeySet().size();
    }
}
