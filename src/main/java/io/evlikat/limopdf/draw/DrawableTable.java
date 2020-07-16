package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.table.PdfColumnDefinition;
import io.evlikat.limopdf.table.PdfTable;
import io.evlikat.limopdf.util.IBlockElement;
import io.evlikat.limopdf.util.grid.Grid;
import io.evlikat.limopdf.util.grid.GridCell;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class DrawableTable implements IBlockElement {

    private final List<ColumnDefinition> columnDefinitions;
    private final Grid<DrawableCell> grid;
    private final PdfTable sourceTable;

    public DrawableTable(PdfTable table) {
        this.sourceTable = table;
        this.columnDefinitions = table.getColumnDefinitions().stream()
            .map(cd -> new ColumnDefinition(cd.getWidth()))
            .collect(toList());
        this.grid = new Grid<>(
            table.getRows().stream()
                .map(r -> r.getCells().stream().map(DrawableCell::new).collect(toList()))
                .collect(toList()));

        applyCellWidth();
    }

    private void applyCellWidth() {
        int gridWidth = grid.getGridWidth();
        int gridHeight = grid.getGridHeight();

        for (int gridRow = 0; gridRow < gridHeight; gridRow++) {
            for (int gridColumn = 0; gridColumn < gridWidth; gridColumn++) {
                GridCell<DrawableCell> gridCell = grid.getCellAt(gridColumn, gridRow);
                if (gridCell.isInSpan()) {
                    continue;
                }
                DrawableCell cell = gridCell.getCell();
                cell.setWidth((float)
                    columnDefinitions.subList(gridColumn, gridColumn + cell.getColSpan())
                        .stream()
                        .mapToDouble(ColumnDefinition::getWidth)
                        .sum()
                );
            }
        }
    }

    public GridCell<DrawableCell> getCellAt(int gridColumn, int gridRow) {
        return grid.getCellAt(gridColumn, gridRow);
    }

    public float getWidth() {
        return (float) sourceTable.getColumnDefinitions().stream().mapToDouble(PdfColumnDefinition::getWidth).sum();
    }

    @Override
    public float getTopPadding() {
        return sourceTable.getPadding().getTop();
    }

    @Override
    public float getTopMargin() {
        return sourceTable.getMargin().getTop();
    }

    public float getRowHeight(int gridRow) {
        return grid.getRowHeight(gridRow);
    }

    @Override
    public float getHeight() {
        return (float) IntStream.range(0, grid.getGridHeight()).mapToDouble(grid::getRowHeight).sum();
    }

    public int getGridHeight() {
        return grid.getGridHeight();
    }

    public int getGridWidth() {
        return grid.getGridWidth();
    }
}

