package io.evlikat.limopdf.structure;

import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.page.PageSpecification;

import java.util.Optional;

public interface Drawable {

    Drawer drawer();

    /**
     * TODO: move this out the interface
     * if not empty returns next page specification must be immediately started after this {@link Drawable}
     */
    Optional<PageSpecification> pageBreak();
}
