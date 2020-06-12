package io.evlikat.limopdf.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class Box {

    private final float top;
    private final float right;
    private final float bottom;
    private final float left;

    public static Box of(float value) {
        return new Box(value, value, value, value);
    }

    public static Box of(float topBottom, float rightLeft) {
        return new Box(topBottom, rightLeft, topBottom, rightLeft);
    }

    public static Box of(float top, float rightLeft, float bottom) {
        return new Box(top, rightLeft, bottom, rightLeft);
    }
}
