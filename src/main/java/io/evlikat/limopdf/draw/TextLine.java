package io.evlikat.limopdf.draw;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class TextLine {

    private float height = 0f;
    private float width = 0f;
    private final List<TextChunk> chunks;

    public TextLine() {
        this(Collections.emptyList());
    }

    public TextLine(List<TextChunk> chunks) {
        this.chunks = new ArrayList<>(chunks);
    }

    @SneakyThrows
    public void addChunk(TextChunk chunk) {
        this.height = Math.max(height, chunk.getCharacterProperties().getHeightInPixels());
        this.width += chunk.getWidth();
        this.chunks.add(chunk);
    }

    public boolean anyChunk() {
        return !this.chunks.isEmpty();
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }
}
