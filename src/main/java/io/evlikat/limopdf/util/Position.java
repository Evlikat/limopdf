package io.evlikat.limopdf.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class Position {
    private final float x;
    private final float y;

    public Position minusY(float dy) {
        return new Position(x, y - dy);
    }
}
