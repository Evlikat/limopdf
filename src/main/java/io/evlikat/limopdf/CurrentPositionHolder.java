package io.evlikat.limopdf;

import io.evlikat.limopdf.util.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CurrentPositionHolder {

    private Position position;

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public void minusY(float dy) {
        position = position.minusY(dy);
    }
}
