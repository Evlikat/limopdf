package io.evlikat.limopdf;

import io.evlikat.limopdf.structure.Drawable;
import io.evlikat.limopdf.structure.StickyDrawable;

import java.util.Collection;

public interface DrawableSink {

    void add(Drawable stickyDrawable);

    void add(StickyDrawable stickyDrawable, Collection<Drawable> otherDrawables);
}
