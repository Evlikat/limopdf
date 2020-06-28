package io.evlikat.limopdf.structure;

import io.evlikat.limopdf.StickyDrawer;

public interface StickyDrawable extends Drawable {

    @Override
    StickyDrawer drawer();

    boolean isKeepWithNext();
}
