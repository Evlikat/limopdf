package io.evlikat.limopdf.structure;

import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.page.PageSpecification;

import java.util.Optional;

public interface Drawable {

    Drawer drawer();

    /**
     * if not empty returns next page specification must be immediately started after this {@link Drawable}
     */
    Optional<PageSpecification> pageBreak();
}
