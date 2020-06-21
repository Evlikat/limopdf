package io.evlikat.limopdf.page;

import io.evlikat.limopdf.util.Box;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageSpecification {

    private Box margin = Box.of(50f, 50f);
    private PDRectangle size = PDRectangle.A4;
}
