package io.evlikat.limopdf.page;

import io.evlikat.limopdf.util.Box;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

@Getter
@Setter
@NoArgsConstructor
public class PageSpecification {

    private final Box margin = Box.of(75f, 50);
    private final PDRectangle size = PDRectangle.A4;
}