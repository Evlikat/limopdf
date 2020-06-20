package io.evlikat.limopdf.util;

import lombok.Data;

@Data(staticConstructor = "of")
public class SimpleBlockElement implements IBlockElement {

    private final float height;
    private final float topPadding;
    private final float topMargin;
}
