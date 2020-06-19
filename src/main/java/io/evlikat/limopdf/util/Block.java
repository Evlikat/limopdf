package io.evlikat.limopdf.util;

import lombok.Data;

@Data(staticConstructor = "of")
public class Block implements IBlock {
    private final float height;
}
