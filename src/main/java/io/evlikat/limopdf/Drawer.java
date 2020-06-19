package io.evlikat.limopdf;

import io.evlikat.limopdf.draw.DrawableArea;

public interface Drawer {

    /**
     * Draws an object on the {@link DrawableArea} considering its bounds.
     * This method can be invoked more than once if {@link DrawResult} is not {@link DrawResult#COMPLETE}.
     *
     * @param drawableArea a canvas to draw of controlled height
     * @return Draw Result: {@link DrawResult#COMPLETE} if the object was drawn completely to this drawable area,
     * {@link DrawResult#PARTIAL} or {@link DrawResult#REJECTED} if the object must be moved to the next page.
     */
    DrawResult draw(DrawableArea drawableArea);
}
