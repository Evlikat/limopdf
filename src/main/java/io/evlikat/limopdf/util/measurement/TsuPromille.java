package io.evlikat.limopdf.util.measurement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class TsuPromille {

    private final float value;

    public float toPixels(float fontSize) {
        return value / 1000 * fontSize;
    }
}
