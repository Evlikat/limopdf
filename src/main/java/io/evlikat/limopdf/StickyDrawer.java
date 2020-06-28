package io.evlikat.limopdf;

import io.evlikat.limopdf.structure.Drawable;

import java.util.Collection;

public interface StickyDrawer extends Drawer {

    void addFollowingDrawables(Collection<Drawable> drawables);
}
