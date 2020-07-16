package io.evlikat.limopdf.util.grid;

public interface ICell {

    int getRowSpan();

    int getColSpan();

    float getWidth();

    void setWidth(float width);

    float getHeight();
}
