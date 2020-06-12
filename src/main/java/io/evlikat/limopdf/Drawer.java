package io.evlikat.limopdf;

import io.evlikat.limopdf.draw.DrawableArea;

import java.io.IOException;

public interface Drawer {

    void draw(DrawableArea drawableArea) throws IOException;
}
