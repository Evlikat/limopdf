package io.evlikat.limopdf.util;

public class Measurement {

    public static float tsuPromilleToPixels(float value, float fontSizePx) {
        return value / 1000 * fontSizePx;
    }
}
