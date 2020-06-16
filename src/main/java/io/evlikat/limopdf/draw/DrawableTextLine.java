package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.util.IRectangle;
import lombok.Data;

@Data
public class DrawableTextLine implements IRectangle {

    private final TextLine line;
    private final float leftIndent;

    @Override
    public float getWidth() {
        return line.getWidth();
    }

    @Override
    public float getHeight() {
        return line.getHeight();
    }
}
