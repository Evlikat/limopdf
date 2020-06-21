package io.evlikat.limopdf.page;

import io.evlikat.limopdf.util.Box;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PageSpecifications {

    public static final PageSpecification A4 = new PageSpecification();
    public static final PageSpecification A5 = new PageSpecification(Box.of(50f), PDRectangle.A5);

    private PageSpecifications() {
    }
}
