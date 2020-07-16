package io.evlikat.limopdf.table;

import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.page.PageSpecification;
import io.evlikat.limopdf.structure.Drawable;
import io.evlikat.limopdf.util.Box;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class PdfTable implements Drawable {

    @Getter
    @Setter
    private Box margin = Box.of(0f);

    @Getter
    @Setter
    private Box padding = Box.of(0f);

    @Getter
    private final List<PdfColumnDefinition> columnDefinitions;
    @Getter
    private final List<PdfRow> rows = new ArrayList<>();

    public PdfTable(float... columnWidth) {
        this.columnDefinitions = IntStream.range(0, columnWidth.length)
            .mapToObj(i -> new PdfColumnDefinition(columnWidth[i]))
            .collect(toList());
    }

    public PdfTable(List<PdfColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    public void addRow(PdfRow row) {
        this.rows.add(row);
    }

    public void addRows(List<PdfRow> rows) {
        this.rows.addAll(rows);
    }

    @Override
    public Drawer drawer() {
        return new PdfTableDrawer(this);
    }

    @Override
    public Optional<PageSpecification> pageBreak() {
        return Optional.empty();
    }
}
