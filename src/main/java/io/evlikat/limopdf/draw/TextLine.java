package io.evlikat.limopdf.draw;

import io.evlikat.limopdf.util.IRectangle;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TextLine implements IRectangle {

    private float height = 0f;
    private float width = 0f;
    private final List<TextChunk> chunks = new ArrayList<>();

    public TextLine() {
    }

    public TextLine(List<TextChunk> chunks) {
        chunks.forEach(this::addChunk);
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
}
