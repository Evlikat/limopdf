package io.evlikat.limopdf.util.color;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Color {

    public static final Color BLACK = new Color(java.awt.Color.BLACK);
    public static final Color RED = new Color(java.awt.Color.RED);
    public static final Color GENTLE_GREEN = new Color(new java.awt.Color(32, 96, 32, 161));

    @Getter
    private final java.awt.Color nativeColor;
}
