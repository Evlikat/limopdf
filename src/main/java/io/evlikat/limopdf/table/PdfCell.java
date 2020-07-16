package io.evlikat.limopdf.table;

import io.evlikat.limopdf.structure.Drawable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class PdfCell {

    @Getter
    @Setter
    private int colSpan = 1;

    @Getter
    @Setter
    private int rowSpan = 1;

    @Getter
    private final List<Drawable> content = new ArrayList<>();

    public void addDrawable(Drawable drawable) {
        this.content.add(drawable);
    }

    public void addDrawables(List<Drawable> drawables) {
        this.content.addAll(drawables);
    }
}
