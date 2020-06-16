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

    public static Box top(float value) {
        return new Box(value, 0f, 0f, 0f);
    }

    public static Box topBottom(float value) {
        return new Box(value, 0f, value, 0f);
    }

    public static Box right(float value) {
        return new Box(0f, value, 0f, 0f);
    }

    public static Box bottom(float value) {
        return new Box(0f, 0f, value, 0f);
    }

    public static Box left(float value) {
        return new Box(0f, 0f, 0f, value);
    }

    public static Box rightLeft(float value) {
        return new Box(0f, value, 0f, value);
    }

    public static Box of(float topBottom, float rightLeft) {
        return new Box(topBottom, rightLeft, topBottom, rightLeft);
    }

    public static Box of(float top, float rightLeft, float bottom) {
        return new Box(top, rightLeft, bottom, rightLeft);
    }
}
